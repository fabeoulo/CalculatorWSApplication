/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Pending;
import com.advantech.model.User;
import com.advantech.model.Type;
import com.advantech.model.Worktime;
import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.UserService;
import com.advantech.service.PendingService;
import com.advantech.service.TypeService;
import com.advantech.service.WorktimeService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application file upload requests
 */
@Controller
@Secured({"ROLE_ADMIN"})
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private UserService userService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FlowService flowService;

    private static List<Worktime> l;

    /**
     * Upload single file using Spring Controller
     *
     * @param action
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile.do", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam String action, @RequestParam("file") MultipartFile file) {
        String message = "";

        Workbook workbook = null;
        int i = 0;
        if (l == null) {
            l = new ArrayList();
        }

        InputStream inputStream = null;

        try {
            inputStream = file.getInputStream();
            if (l.isEmpty()) {
                Map floorOptions = this.tranToIdNameCompare(floorService.findAll());
                Map userOptions = this.tranToIdNameCompare(userService.findAll());
                Map typeOptions = this.tranToIdNameCompare(typeService.findAll());
                Map flowOptions = this.tranToIdNameCompare(flowService.findAll());

                workbook = WorkbookFactory.create(inputStream);

                Sheet sheet = workbook.getSheetAt(0);

                int maxNumberfRows = sheet.getPhysicalNumberOfRows();
                User sysop = (User) userOptions.get("sysop"); //sysop
                Type type_ok = (Type) typeOptions.get("OK"); //ok
                Floor floor_all = (Floor) floorOptions.get("All"); //all
                Pending default_pending = pendingService.findByPrimaryKey(1); //AASSY

                for (i = 2; i < maxNumberfRows; i++) {
                    // 由於第 0 Row 為 title, 故 i 從 1 開始

                    Row row = sheet.getRow(i); // 取得第 i Row
                    if (row != null) {
                        Cell cell_A = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                        cell_A.setCellType(CellType.STRING);

                        Worktime w = new Worktime();
                        w.setFloor((Floor) isNull(floorOptions.get(getCellValue(row, "V")), floor_all));
                        w.setFlowByTestFlowId((Flow) flowOptions.get(trimStringObject(getCellValue(row, "AG"))));
                        w.setFlowByPackingFlowId((Flow) flowOptions.get(trimStringObject(getCellValue(row, "AH"))));
                        w.setFlowByBabFlowId((Flow) flowOptions.get(trimStringObject(getCellValue(row, "AF"))));
                        w.setUserByEeOwnerId((User) isNull(userOptions.get(getCellValue(row, "AA")), sysop));
                        w.setUserByQcOwnerId((User) isNull(userOptions.get(getCellValue(row, "AB")), sysop));
                        w.setUserBySpeOwnerId((User) isNull(userOptions.get(getCellValue(row, "Z")), sysop));
                        w.setType((Type) isNull(typeOptions.get(getCellValue(row, "B")), type_ok));
                        w.setModelName(((String) getCellValue(row, "A")).replaceAll("[^a-zA-Z]+", ""));
                        w.setTotalModule(objToBigDecimal(getCellValue(row, "D")));
                        w.setCleanPanel(objToBigDecimal(getCellValue(row, "F")));
                        w.setAssy(objToBigDecimal(getCellValue(row, "G")));
                        w.setT1(objToBigDecimal(getCellValue(row, "H")));
                        w.setT2(objToBigDecimal(getCellValue(row, "I")));
                        w.setT3(objToBigDecimal(getCellValue(row, "J")));
                        w.setT4(objToBigDecimal(getCellValue(row, "K")));
                        w.setPacking(objToBigDecimal(getCellValue(row, "L")));
                        w.setUpBiRi(objToBigDecimal(getCellValue(row, "M")));
                        w.setDownBiRi(objToBigDecimal(getCellValue(row, "N")));
                        w.setBiCost(objToBigDecimal(getCellValue(row, "O")));
                        w.setVibration(objToBigDecimal(getCellValue(row, "P")));
                        w.setHiPotLeakage(objToBigDecimal(getCellValue(row, "Q")));
                        w.setColdBoot(objToBigDecimal(getCellValue(row, "R")));
                        w.setWarmBoot(objToBigDecimal(getCellValue(row, "S")));
                        w.setBurnIn(getCellValue(row, "W") == null ? "N" : "Y");

                        BigDecimal biTime = objToBigDecimal(getCellValue(row, "X"));
                        w.setBiTime(biTime == null ? BigDecimal.ZERO : biTime); //Not null

                        BigDecimal biTemperature = objToBigDecimal(getCellValue(row, "Y"));
                        w.setBiTemperature(biTemperature == null ? BigDecimal.ZERO : biTemperature); //Not null

                        BigDecimal keypartA = objToBigDecimal(getCellValue(row, "AD"));
                        w.setKeypartA(keypartA == null ? null : keypartA.intValue());

                        BigDecimal keypartB = objToBigDecimal(getCellValue(row, "AE"));
                        w.setKeypartB(keypartB == null ? null : keypartB.intValue());
                        w.setPartLink(getCellValue(row, "AI") == null ? null : ((String) getCellValue(row, "AI")).charAt(0));
                        w.setCe(getCellValue(row, "AJ") == null ? 0 : 1);
                        w.setUl(getCellValue(row, "AK") == null ? 0 : 1);
                        w.setRohs(getCellValue(row, "AL") == null ? 0 : 1);
                        w.setWeee(getCellValue(row, "AM") == null ? 0 : 1);
                        w.setMadeInTaiwan(getCellValue(row, "AN") == null ? 0 : 1);
                        w.setFcc(getCellValue(row, "AO") == null ? 0 : 1);
                        w.setEac(getCellValue(row, "AP") == null ? 0 : 1);
                        w.setNsInOneCollectionBox(objToBigDecimal(getCellValue(row, "AQ")));
                        w.setPartNoAttributeMaintain(getCellValue(row, "AR") == null ? 'N' : (getCellValue(row, "AR")).toString().charAt(0));
                        w.setAssyLeadTime(objToBigDecimal(getCellValue(row, "AU")));
                        w.setPackingLeadTime(objToBigDecimal(getCellValue(row, "AW")));
                        w.setProductionWt(objToBigDecimal(getCellValue(row, "C")));
                        w.setSetupTime(objToBigDecimal(getCellValue(row, "E")));
                        w.setAssyToT1(objToBigDecimal(getCellValue(row, "T")));
                        w.setT2ToPacking(objToBigDecimal(getCellValue(row, "U")));

                        BigDecimal assyStation = objToBigDecimal(getCellValue(row, "AS"));
                        w.setAssyStation(assyStation == null ? null : assyStation.intValue());

                        BigDecimal packingStation = objToBigDecimal(getCellValue(row, "AT"));
                        w.setPackingStation(packingStation == null ? null : packingStation.intValue());

                        w.setAssyKanbanTime(objToBigDecimal(getCellValue(row, "AV")));
                        w.setPackingKanbanTime(objToBigDecimal(getCellValue(row, "AX")));
                        w.setCleanPanelAndAssembly(objToBigDecimal(getCellValue(row, "BB")));
                        w.setPending(default_pending);
                        w.setPendingTime(BigDecimal.ZERO);
                        w.setAssyPackingSop(getCellValue(row, "AC") == null ? null : (String) getCellValue(row, "AC"));

                        l.add(w);
                    }
                }
            }
//            worktimeService.saveOrUpdate(l);
            message = "Data init done.";
        } catch (IOException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | EncryptedDocumentException | InvalidFormatException ex) {
            System.out.println(ex);
            message = ex.getMessage();
        } catch (Exception ex) {
            System.out.println(ex);
            message = "Error initialize object at row number " + (i + 1);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        return message;
    }

    @ResponseBody
    @RequestMapping(value = "/checkWorktime", method = RequestMethod.POST)
    public String checkWorktimeHandler(@RequestParam("file") MultipartFile file) {
        String message = "";

        Workbook workbook = null;
        int i = 0;

        InputStream inputStream = null;

        try {
            inputStream = file.getInputStream();

            Map floorOptions = this.tranToIdNameCompare(floorService.findByPrimaryKeys(1, 2));
            Map userOptions = this.tranToIdNameCompare(userService.findAll());
            Map typeOptions = this.tranToIdNameCompare(typeService.findByPrimaryKeys(6, 9, 10));

            workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            int maxNumberfRows = sheet.getPhysicalNumberOfRows();

            CellStyle alert_style = workbook.createCellStyle();
            Font redFont = workbook.createFont();
            redFont.setColor(Font.COLOR_RED);
            alert_style.setFont(redFont);
            alert_style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            alert_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle title_alert = workbook.createCellStyle();
            title_alert.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            title_alert.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (i = 2; i < maxNumberfRows; i++) {
                // 由於第 0 Row 為 title, 故 i 從 1 開始

                Row row = sheet.getRow(i); // 取得第 i Row

                Cell cell_A = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                cell_A.setCellType(CellType.STRING);

                boolean checkFlag = true;

                if (floorOptions.get(getCellValue(row, "V")) == null) {
                    cellSetAlert(row, "V", alert_style);
                    checkFlag = false;
                }

                Object aa_value = getCellValue(row, "AA");
                if (aa_value == null || userOptions.get(aa_value.toString().toLowerCase()) == null) {
                    cellSetAlert(row, "AA", alert_style);
                    checkFlag = false;
                }

                Object ab_value = getCellValue(row, "AB");
                if (ab_value == null || userOptions.get(ab_value.toString().toLowerCase()) == null) {
                    cellSetAlert(row, "AB", alert_style);
                    checkFlag = false;
                }

                Object z_value = getCellValue(row, "Z");
                if (z_value == null || userOptions.get(z_value.toString().toLowerCase()) == null) {
                    cellSetAlert(row, "Z", alert_style);
                    checkFlag = false;
                }
                if (typeOptions.get(getCellValue(row, "B")) == null) {
                    cellSetAlert(row, "B", alert_style);
                    checkFlag = false;
                }

                if (getCellValue(row, "W") == null) {
                    cellSetAlert(row, "W", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "X")) == null) {
                    cellSetAlert(row, "X", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "Y")) == null) {
                    cellSetAlert(row, "Y", alert_style);
                    checkFlag = false;
                }

//                Object partNoAttrMaintain = getCellValue(row, "AR");
//                if (partNoAttrMaintain == null || !(partNoAttrMaintain.toString().trim().equals("N") || partNoAttrMaintain.toString().trim().equals("Y"))) {
//                    cellSetAlert(row, "AR", alert_style);
//                    checkFlag = false;
//                }

                if (objToBigDecimal(getCellValue(row, "AS")) == null) {
                    cellSetAlert(row, "AS", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "AT")) == null) {
                    cellSetAlert(row, "AT", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "C")) == null) {
                    cellSetAlert(row, "C", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "E")) == null) {
                    cellSetAlert(row, "E", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "T")) == null) {
                    cellSetAlert(row, "T", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "U")) == null) {
                    cellSetAlert(row, "U", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "AS")) == null) {
                    cellSetAlert(row, "AS", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "AT")) == null) {
                    cellSetAlert(row, "AT", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "AV")) == null) {
                    cellSetAlert(row, "AV", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "AX")) == null) {
                    cellSetAlert(row, "AX", alert_style);
                    checkFlag = false;
                }

                if (objToBigDecimal(getCellValue(row, "BB")) == null) {
                    cellSetAlert(row, "BB", alert_style);
                    checkFlag = false;
                }

                Object babFlow = getCellValue(row, "AF");
                Object testFlow = getCellValue(row, "AG");
                if (babFlow != null && testFlow != null) {
                    boolean flowCheckFlag = flowService.checkFlowInGroup(babFlow.toString().replaceAll("[^a-zA-Z0-9\\_\\-\\\\(\\\\)]+", ""), testFlow.toString().trim().replaceAll("[^a-zA-Z0-9\\_\\-\\\\(\\\\)]+", ""));
                    if (flowCheckFlag == false) {
                        cellSetAlert(row, "AF", alert_style);
                        cellSetAlert(row, "AG", alert_style);
                        checkFlag = false;
                    }
                }

                if (checkFlag == false) {
                    cellSetAlert(row, "A", title_alert);
                    Cell cell = CellUtil.getCell(row, CellReference.convertColStringToIndex("BG"));
                    cell.setCellValue(1);
                }
            }

            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "tmpFiles");
            if (!dir.exists()) {
                dir.mkdirs();
                // Create the file on server
            }

            File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + file.getOriginalFilename());

            try (FileOutputStream stream = new FileOutputStream(serverFile)) {
                workbook.write(stream);
            }

            System.out.println("File has been upload.");

            message = "Data init done.";
        } catch (IOException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | EncryptedDocumentException | InvalidFormatException ex) {
            System.out.println(ex);
            message = ex.getMessage();
        } catch (Exception ex) {
            System.out.println(ex);
            message = "Error initialize object at row number " + (i + 1);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        return message;
    }

    public static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }

    private Object isNull(Object i, Object replaceTarget) {
        return i == null ? replaceTarget : i;
    }

    private Object trimStringObject(Object o) {
        return o == null ? o : o.toString().trim();
    }

    private BigDecimal objToBigDecimal(Object o) {
        return o != null && NumberUtils.isNumber(o.toString()) ? new BigDecimal(o.toString()) : null;
    }

    private Object getCellValue(Row row, String letter) {
        Cell cell = CellUtil.getCell(row, CellReference.convertColStringToIndex(letter));
        CellType cellType = cell.getCellTypeEnum();
        if (null == cellType) {
            return null;
        } else {
            switch (cellType) {
                case STRING:
                    String value = cell.getStringCellValue();
                    return value == null || "".equals(value.trim()) ? null : value;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            return cell.getNumericCellValue();
                        case Cell.CELL_TYPE_STRING:
                            return null;
                    }
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return cell.getNumericCellValue();
                    }
                case BLANK:
                    return null;
                case BOOLEAN:
                    return Boolean.toString(cell.getBooleanCellValue());
                default:
                    return null;
            }
        }
    }

    private void cellSetAlert(Row row, String letter, CellStyle style) {
        Cell cell = CellUtil.getCell(row, CellReference.convertColStringToIndex(letter));
        cell.setCellStyle(style);
    }

    private Map tranToIdNameCompare(List l) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map m = new HashMap();
        if (!l.isEmpty()) {
            Object firstObj = l.get(0);
            boolean isUserObject = firstObj instanceof User;
            for (Object obj : l) {
                String name = (String) PropertyUtils.getProperty(obj, isUserObject ? "username" : "name");
                m.put(isUserObject ? name.toLowerCase() : name, obj);
            }
        }
        return m;
    }

    /**
     * Upload multiple file using Spring Controller
     *
     * @param model
     * @param files
     * @return
     */
    @RequestMapping(value = "/uploadMultipleFile.do", method = RequestMethod.POST)
    public String uploadMultipleFileHandler(Model model, @RequestParam("file") MultipartFile[] files) {

        String[] message = this.copyFileToServer(files);
        model.addAttribute("message", message);

        return "forward:fileupload.jsp";
    }

    private String[] copyFileToServer(MultipartFile... multipartFiles) {
        List<String> message = new ArrayList();

        // Creating the directory to store file
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "tmpFiles");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile file : multipartFiles) {
            System.out.println(file.getContentType());

            String name = file.getOriginalFilename();
            try {
                byte[] bytes = file.getBytes();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
                try (BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile))) {
                    stream.write(bytes);
                }

                logger.info("Server File Location=" + serverFile.getAbsolutePath());

                message.add("You successfully uploaded file: " + name + " ");
            } catch (IOException e) {
                message.add("You failed to upload => " + e.getMessage());
            }
        }
        return message.toArray(new String[message.size()]);
    }
}