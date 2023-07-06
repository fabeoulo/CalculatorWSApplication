/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabCollectModeChangeEvent;
import com.advantech.model.db1.BabDataCollectMode;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.Fqc;
import com.advantech.model.db1.FqcLine;
import com.advantech.model.db1.Line;
import com.advantech.model.db1.LineType;
import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.model.db1.PreAssyModuleType;
import com.advantech.model.db1.PrepareSchedule;
import com.advantech.model.db1.PrepareScheduleEndtimeSetting;
import com.advantech.model.db1.TagNameComparison;
import com.advantech.model.db1.User;
import com.advantech.model.db1.Worktime;
import com.advantech.model.db3.WorktimeM3;
import com.advantech.model.view.db1.BabProcessDetail;
import com.advantech.quartzJob.HandleUncloseBab;
import com.advantech.service.db1.BabCollectModeChangeEventService;
import com.advantech.service.db1.BabPassStationRecordService;
import com.advantech.service.db1.BabSensorLoginRecordService;
import com.advantech.service.db1.BabService;
import com.advantech.service.db1.BabSettingHistoryService;
import com.advantech.service.db1.FloorService;
import com.advantech.service.db1.FqcLineService;
import com.advantech.service.db1.FqcProductivityHistoryService;
import com.advantech.service.db1.FqcService;
import com.advantech.service.db2.LineBalancingService;
import com.advantech.service.db1.LineService;
import com.advantech.service.db1.LineTypeService;
import com.advantech.service.db1.LineUserReferenceService;
import com.advantech.service.db1.ModelSopRemarkDetailService;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import com.advantech.service.db1.PreAssyModuleTypeService;
import com.advantech.service.db1.PrepareScheduleEndtimeSettingService;
import com.advantech.service.db1.PrepareScheduleService;
import com.advantech.service.db1.SystemReportService;
import com.advantech.service.db1.TagNameComparisonService;
import com.advantech.service.db1.UserService;
import com.advantech.service.db1.WorktimeService;
import com.advantech.service.db3.SqlViewService;
import com.advantech.service.db3.WorktimeM3Service;
import com.advantech.webservice.WebServiceRV;
import com.fasterxml.jackson.core.JsonProcessingException;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {

    @Autowired
    private BabService babService;

    @Autowired
    private LineBalancingService lineBalancingService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private UserService userService;

    @Autowired
    private BabLineTypeFacade bf;

    @Autowired
    private CustomPasswordEncoder encoder;

    @Autowired
    private FqcService fqcService;

    @Autowired
    private FqcLineService fqcLineService;

    @Value("${endpoint.quartz.trigger}")
    private String endpointPollingCron;

    @Autowired
    private FqcProductivityHistoryService productivityService;

    @Autowired
    private BabPassStationRecordService passStationService;

    @Autowired
    private BabCollectModeChangeEventService babCollectModeChangeEventService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testLineBalancingService() throws JobExecutionException {
        new HandleUncloseBab().executeInternal(null);
    }

    @Autowired
    private PreAssyModuleTypeService preAssyModuleTypeService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testPreAssyModuleTypeService() throws JobExecutionException {
        int lineType_id = 1;
        LineType lt = lineTypeService.findByPrimaryKey(lineType_id);
        checkState(lt != null, "Can't find lineType in id " + lineType_id);
        List<PreAssyModuleType> l = preAssyModuleTypeService.findByModelNameAndLineType("TPC1282T533A2102-T", lt);
        new HandleUncloseBab().executeInternal(null);
    }

    @Autowired
    private PreAssyModuleStandardTimeService preAssyModuleStandardTimeService;
    @Autowired
    private SystemReportService systemReportService;

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testSetPreAssyModuleStandardTime() throws JobExecutionException {
        String sds = new DateTime().minusMonths(1).toString("yyyy-MM-dd");
        String eds = new DateTime().toString("yyyy-MM-dd");
        List<Map> data = systemReportService.getBabPreAssyDetailForExcel(-1, -1, sds, eds);

        Map<String, BigDecimal> mapM3Wt = getPreAssyStandardTime(data, Arrays.asList("5", "6"));
        Map<String, BigDecimal> mapM6Wt = getPreAssyStandardTime(data, Arrays.asList("7"));

//        List<String> keys = new ArrayList<>();
//        keys.addAll(mapM3Wt.keySet());
//        keys.addAll(mapM6Wt.keySet());
//        List<Integer> typeIds = Arrays.asList(44, 322);
        List<PreAssyModuleStandardTime> ls = preAssyModuleStandardTimeService.findAllWithTypes();
        Map<String, Long> collect2 = ls.stream()
                .filter(p -> p.getPreAssyModuleType().getName().startsWith("(前置"))
                .collect(Collectors.groupingBy(ps -> ps.getModelName(), Collectors.counting()
                ));

//        ls = ls.stream().filter(t -> keys.contains(t.getModelName()) && typeIds.contains(t.getPreAssyModuleType().getId()))
//                .collect(Collectors.toList());

        List<String> m3Linetype = Arrays.asList("ASSY");
        List<String> m6Linetype = Arrays.asList("Cell");
        ls = ls.stream().filter(e -> {
            String key = e.getModelName() + "_" + e.getPreAssyModuleType().getName();
            String moduleLinetype = e.getPreAssyModuleType().getLineType().getName();
            HibernateObjectPrinter.print(e.getModelName());
            if (e.getModelName().equals("CRV430WP2102-T")) {
                HibernateObjectPrinter.print(e.getPreAssyModuleType());
            }

            if (m3Linetype.contains(moduleLinetype) && mapM3Wt.containsKey(key)) {
                e.setStandardTime(mapM3Wt.get(key));
                return true;
            } else if (m6Linetype.contains(moduleLinetype) && mapM6Wt.containsKey(key)) {
                e.setStandardTime(mapM6Wt.get(key));
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        preAssyModuleStandardTimeService.update(ls);
    }

    private Map<String, BigDecimal> getPreAssyStandardTime(List<Map> data, List<String> floorName) {
        Map<String, BigDecimal> mapSt = new HashMap<>();
        data.stream().filter(m
                -> floorName.contains(m.get("sitefloor").toString())
                && m.get("modelName") != null
                && m.get("preModuleName") != null
        )
                .collect(Collectors.groupingBy(map -> map.get("modelName").toString() + "_" + map.get("preModuleName").toString()))
                .forEach((key, value) -> {
                    int pcs = value.stream().mapToInt(v -> (int) v.get("pcs")).sum();
                    int spend = value.stream().mapToInt(v -> (int) v.get("時間花費")).sum();
                    BigDecimal swt = new BigDecimal(spend / pcs);
                    mapSt.put(key, swt);
                });
        return mapSt;
    }

//    @Test
//    @Transactional
//    //@Rollback(true)
    public void testPreAssyModuleStandardTimeService() throws JobExecutionException {
        List<PreAssyModuleStandardTime> ps1s = preAssyModuleStandardTimeService.findAll();
        Map<String, Long> collect2 = ps1s.stream()
                .filter(p -> p.getPreAssyModuleType().getName().startsWith("(前置"))
                .collect(Collectors.groupingBy(ps -> ps.getModelName(), Collectors.counting()
                ));

        List<WorktimeM3> listM3 = worktimeM3Service.findByModel(collect2.keySet().stream().collect(Collectors.toList()));

        listM3.forEach(e -> {
//                    System.out.println(e.getModelName()+ " - " + collect2.get(e.getModelName()));
            e.setPreAssyModuleQty(collect2.get(e.getModelName()).intValue());
            worktimeM3Service.update(e);

//                    List<WorktimeM3> l = worktimeM3Service.findByModel(Arrays.asList(e.getKey()));
//                    if (!l.isEmpty()) {
//                        WorktimeM3 existRecord = l.get(0);
//                    }
        });
    }

    @Autowired
    private WorktimeM3Service worktimeM3Service;

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testpreAssyModuleStandardTimeService() throws JobExecutionException {
        String s1 = "POCS1991703-T";
        List<String> modelName = new ArrayList<>();
        modelName.add(s1);
        List<WorktimeM3> lt = worktimeM3Service.findByModel(modelName);
        checkState(lt != null, "Can't find lineType in id ");
        HibernateObjectPrinter.print(lt);

        WorktimeM3 w3 = lt.get(0);
        w3.setPreAssyModuleQty(-1);
        worktimeM3Service.update(w3);
        HibernateObjectPrinter.print(lt);
    }

//    @Test
//    @Transactional
//    @Rollback(true)
    public void testWorktimeM3Service() throws JobExecutionException {
        String s1 = "TPC1282T533A2102-T";
        List<String> modelName = new ArrayList<>();
        modelName.add(s1);
        List<WorktimeM3> lt = worktimeM3Service.findByModel(modelName);
        checkState(lt != null, "Can't find lineType in id ");
        HibernateObjectPrinter.print(lt);

        WorktimeM3 w3 = lt.get(0);
        w3.setPreAssyModuleQty(-1);
        worktimeM3Service.update(w3);
        HibernateObjectPrinter.print(lt);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testBabSettingHistoryService() {

        BabSettingHistory setting2 = babSettingHistoryService.findFirstProcessingByTagName("PKG_L4-S-2");
        assertNotNull(setting2);

        assertEquals(setting2.getTagName().getName(), "PKG_L4-S-2");
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSensorLoginRecordService() throws JsonProcessingException {

        List l = babSensorLoginRecordService.findByLine(3);
        assertTrue(!l.isEmpty());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testBabSettingHistory() throws JsonProcessingException {

        List<BabSettingHistory> allSettings = babSettingHistoryService.findProcessing();
        Bab b = babService.findByPrimaryKey(12991);

        HibernateObjectPrinter.print(allSettings.get(0));

        List<BabSettingHistory> l = allSettings.stream()
                .filter(rec -> rec.getBab().getId() == b.getId()).collect(toList());

        HibernateObjectPrinter.print(l.get(0));

        assertTrue(!l.isEmpty());

        HibernateObjectPrinter.print(allSettings);
        HibernateObjectPrinter.print(l);
    }

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testHibernateInitialize() {
        TagNameComparison t4 = tagNameComparisonService.findByLampSysTagName("L2-S-4");
        TagNameComparison t7 = tagNameComparisonService.findByLampSysTagName("L2-S-7");
        assertNotNull(t4);
        assertNotNull(t7);

//        babService.checkAndInsert(b, endpointPollingCron, t7);
    }

//    @Test
    public void testBabSaving() {
        try {

            Bab b = babService.findByPrimaryKey(14227);

            lineBalancingService.sendMail(b, 1, 1, 1);

        } catch (MessagingException ex) {

        } catch (Exception ex1) {
            System.out.println(ex1);
        }
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testUserService() {

        int[] ids = {
            33,
            34,
            35,
            36,
            37
        };

        for (int id : ids) {
            User u = userService.findByPrimaryKey(id);
            u.setPassword(encoder.encode(u.getPassword()));
            userService.update(u);
        }
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFqc() {
        Fqc fqc = fqcService.findByPrimaryKey(1);
        assertNotNull(fqc);
        assertEquals(1, fqc.getId());
        assertEquals("PIGB304ZA", fqc.getPo());

        FqcLine fqcLine = fqcLineService.findByPrimaryKey(8);
        assertNotNull(fqcLine);
        assertEquals(8, fqcLine.getId());
        assertEquals("L9", fqcLine.getName());

        assertEquals(fqc.getFqcLine(), fqcLine);
    }

//    @Test
    public void testProductivityService() {

    }

    @Autowired
    private WebServiceRV rv;

//    @Test
    @Transactional
    @Rollback(true)
    public void testBarcode() {
        Bab b = babService.findByPrimaryKey(29710);
        assertNotNull(b);

        String tag1 = "NA-S-1";
        String tag2 = "NA-S-2";

        String barcode = "TPAB810772";

        try {
            //Assert can't add next barcode when current barcode is not finished.
            passStationService.checkStationInfoAndInsert(b, tag1, barcode);
            String nextBc = findBarcode(barcode, 1);
            passStationService.checkStationInfoAndInsert(b, tag1, nextBc);

            assertFalse("test1 fail", true);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        try {
            //Assert barcode can't insert next station when previous station is not finished
            passStationService.checkStationInfoAndInsert(b, tag2, barcode);

            assertFalse("test2 fail", true);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        //Test normal insert
        passStationService.checkStationInfoAndInsert(b, tag1, barcode);
        passStationService.checkStationInfoAndInsert(b, tag2, barcode);
        passStationService.checkStationInfoAndInsert(b, tag2, barcode);

        String nextBc = findBarcode(barcode, 1);

        passStationService.checkStationInfoAndInsert(b, tag1, nextBc);
        passStationService.checkStationInfoAndInsert(b, tag1, nextBc);
        passStationService.checkStationInfoAndInsert(b, tag2, nextBc);
        passStationService.checkStationInfoAndInsert(b, tag2, nextBc);

    }

    private String findBarcode(String barcode, int c) {
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(barcode);
        String st = null;
        while (m.find()) {
            st = m.group();
        }
        barcode = barcode.replace(st, Integer.toString((NumberUtils.createInteger(st)) + c));
        return barcode;
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testBabCollectModeChangeEventService() {
        List<BabCollectModeChangeEvent> event = babCollectModeChangeEventService.findAll();
        assertNotNull(event);
        HibernateObjectPrinter.print(event);

        BabCollectModeChangeEvent newEvent = new BabCollectModeChangeEvent();
        newEvent.setMode(BabDataCollectMode.AUTO);
        babCollectModeChangeEventService.insert(newEvent);

    }

    @Autowired
    private LineUserReferenceService lineUserReferenceService;

    @Autowired
    private LineService lineService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testLineUserReferenceService() {
        User user = userService.findByPrimaryKey(11);
        Set<Line> lines = user.getLines();

//        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFindBabTimeGapPerLine() {
        DateTime sD = new DateTime("2019-12-16");
        DateTime eD = new DateTime("2019-12-20").withTime(23, 0, 0, 0);
        List<BabProcessDetail> l = babService.findBabTimeGapPerLine(sD, eD);
        List<BabProcessDetail> checkD = l.stream().filter(b -> b.getLineId() == 28).collect(toList());
        HibernateObjectPrinter.print(checkD);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSearchGaps() {
        DateTime sD = new DateTime("2019-12-19").withTime(8, 30, 0, 0);
        DateTime eD = new DateTime("2019-12-19").withTime(17, 30, 0, 0);

        List<Bab> l = babService.findByDate(sD, eD);
        l = l.stream().filter(b -> b.getLine().getId() == 28).collect(toList());

        List<Interval> gaps = babService.searchGaps(l, sD, eD);

        HibernateObjectPrinter.print(gaps);
    }

    @Autowired
    private PrepareScheduleService psService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private LineTypeService lineTypeService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testGroupBy() {

        Floor floor = floorService.findByPrimaryKey(1);
        DateTime d = new DateTime().withTime(0, 0, 0, 0);

        List<LineType> lt = lineTypeService.findByPrimaryKeys(1, 2);

        List<PrepareSchedule> l = psService.findByFloorAndLineTypeAndDate(floor, lt, d);
        List<String> modelNames = l.stream().map(s -> s.getModelName()).collect(toList());
        List<Bab> babs = babService.findByModelNames(modelNames);

        Map<String, Map<Line, Long>> historyFitUserSetting = babs.stream()
                //                    .filter(b -> b.getModelName().equals(s.getModelName()))
                .collect(groupingBy(Bab::getModelName,
                        Collectors.groupingBy(Bab::getLine,
                                Collectors.mapping(Bab::getId,
                                        Collectors.counting()))));

        Map m = historyFitUserSetting.get("bbbb");
        System.out.println(m == null || m.isEmpty() ? "Object not exists" : "Object exists");

//        for (PrepareSchedule s : l) {
        //Map<ModelName, Map<Line, Count>>
//            HibernateObjectPrinter.print(historyFitUserSetting);
//            break;
//        }
    }

//    @Test
    public void testUserRole() {
        List<User> users = userService.findByRole("ASSY_USER");

        assertTrue(!users.isEmpty());

        HibernateObjectPrinter.print(users.get(0));
    }

    @Autowired
    private ModelSopRemarkDetailService modelSopRemarkDetailService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testModelSopRemarkDetailService() {
        assertTrue(!modelSopRemarkDetailService.findPeopleMatchDetail("TPC-1582H-433BE", 4).isEmpty());
        assertTrue(modelSopRemarkDetailService.findPeopleMatchDetail("TPC-1582H-4VVV33BE", 1).isEmpty());
    }

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    @Qualifier("sqlViewService3")
    private SqlViewService sqlViewService;

    @Test
    @Rollback(false)
    public void testSyncWorktime() {
        List<Worktime> l = sqlViewService.findWorktime();

        l.forEach(w -> {
            w.setId(0);
            worktimeService.insert(w);
        });
    }

    @Autowired
    private PrepareScheduleEndtimeSettingService psesService;

//    @Test
    @Rollback(false)
    public void testPrepareScheduleEndtimeSetting() {
        PrepareScheduleEndtimeSetting setting = psesService.findByPrimaryKey(1);

        assertNotNull(setting);

        HibernateObjectPrinter.print(setting);

        PrepareScheduleEndtimeSetting newSetting = new PrepareScheduleEndtimeSetting();

        DateTime dt = new DateTime("2020-04-20");
        newSetting.setWeekOfYear(dt.getWeekOfWeekyear());
        newSetting.setScheduleEndtime(dt.toDate());

        psesService.insert(newSetting);
    }

    @Autowired
    private com.advantech.service.db1.TestService testLineTypeService;

    @Test
    public void testTestLineTypeUserCheck() {
        testLineTypeService.checkUserIsAvailable("A-11018");
    }

    @Test
    public void testFindBabByPreAssyModuleType() {
        List l = babService.findByPreAssyModuleType(1, "");
        assertEquals(1, l.size());
    }
}
