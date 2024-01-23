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
import com.advantech.model.db1.Unit;
import com.advantech.model.db1.User;
import com.advantech.model.db1.UserInfoOnMes;
import com.advantech.model.db1.UserProfile;
import com.advantech.model.db1.Worktime;
import com.advantech.model.view.db1.BabProcessDetail;
import com.advantech.quartzJob.HandleUncloseBab;
import com.advantech.security.State;
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
import com.advantech.service.db1.UnitService;
import com.advantech.service.db1.UserProfileService;
import com.advantech.service.db1.UserService;
import com.advantech.service.db1.WorktimeService;
import com.advantech.service.db3.SqlViewService;
import com.advantech.webservice.Factory;
import com.advantech.webservice.WebServiceRV;
import com.fasterxml.jackson.core.JsonProcessingException;
import static com.google.common.base.Preconditions.checkState;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private UnitService unitService;

    @Autowired
    private UserProfileService userProfileService;

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
    private CustomPasswordEncoder pswEncoder;

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testInsertMfgUser() {
//        List<UserInfoOnMes> l = rv.getUsersInfoOnMes(Factory.TWM3);
//        List<UserInfoOnMes> remoteDirectUser = l.stream()
//                .filter(ur -> (ur.getUnitNo() != null && ur.getUnitNo().matches("(A|B|T|P)")))
//                .collect(toList());
//
//        List<User> users = userService.findByRole("PREASSY_USER", "ASSY_USER", "TEST_USER", "PACKING_USER");
//
        Floor f = floorService.findByPrimaryKey(4);
        Unit mfg = unitService.findByPrimaryKey(1);
        List<UserProfile> userProfiles = userProfileService.findAll();
//
//        List<String> newU = Arrays.asList("A-F0389","A-F0297");
//        remoteDirectUser.forEach(ru -> {
//            if(newU.contains(ru.getUserNo())){
//                HibernateObjectPrinter.print(ru.getUserNo() + ru.getUserNameCh());
//            }
//            
//            User matchesUser = users.stream()
//                    .filter(a -> a.getJobnumber().equals(ru.getUserNo()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (matchesUser == null) {
//                HibernateObjectPrinter.print(ru.getUserNo() + ru.getUserNameCh());
//            }
//        });

        List<String> newU = Arrays.asList("A-F0297");//"A-F0389",
        List<User> users = userService.findAll();
        newU.forEach(ru -> {
            User matchesUser = users.stream()
                    .filter(a -> a.getJobnumber().equals(ru))
                    .findFirst()
                    .orElse(null);
            if (matchesUser == null) {
                User user = new User();
                user.setJobnumber(ru);
                user.setUsername(ru);
                user.setUsernameCh(ru);
                user.setPassword(pswEncoder.encode(ru));
                user.setState(State.ACTIVE);

                user.setFloor(f);
                user.setUnit(mfg);
                setUserProfle(user, "B", userProfiles);

                userService.insert(user);
                System.out.println("insert" + " " + ru);
            }
        });
    }

    private void setUserProfle(User user, String department, List<UserProfile> userProfiles) {
        UserProfile preAssyRole = userProfiles.stream().filter(p -> p.getId() == 19).findFirst().orElse(null);
        UserProfile assyRole = userProfiles.stream().filter(p -> p.getId() == 14).findFirst().orElse(null);
        UserProfile pkgRole = userProfiles.stream().filter(p -> p.getId() == 15).findFirst().orElse(null);
        UserProfile testRole = userProfiles.stream().filter(p -> p.getId() == 16).findFirst().orElse(null);

        Set<UserProfile> roles = user.getUserProfiles();
        roles.remove(preAssyRole);
        roles.remove(assyRole);
        roles.remove(testRole);
        roles.remove(pkgRole);
//"ASSY_USER", "PREASSY_USER", "PACKING_USER"

        switch (department) {
            case "A":
                roles.add(preAssyRole);
                break;
            case "B":
                roles.add(assyRole);
                break;
            case "T":
                roles.add(testRole);
                break;
            case "P":
                roles.add(pkgRole);
                break;
            default:
                break;
        }

        user.setUserProfiles(roles);
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

    @Test
    @Transactional
//    @Rollback(false)
    public void testSetPreAssyModuleStandardTime() throws JobExecutionException {
        String sds = new DateTime().minusMonths(1).toString("yyyy-MM-dd");
        String eds = new DateTime().toString("yyyy-MM-dd");
        List<Map> data = systemReportService.getBabPreAssyDetailForExcel(-1, -1, sds, eds);

//        Map<String, BigDecimal> mapM3WtInExcel = getPreAssyStandardTime(data, Arrays.asList("5", "6"));
//        Map<String, BigDecimal> mapM6WtInExcel = getPreAssyStandardTime(data, Arrays.asList("7"));
        Map<String, BigDecimal> mapWtInExcel = getPreAssyStandardTime(data);

        List<PreAssyModuleStandardTime> ls = preAssyModuleStandardTimeService.findAllWithTypes();

//        List<String> m3Linetype = Arrays.asList("ASSY");
//        List<String> m6Linetype = Arrays.asList("Cell");
        ls = ls.stream()
                .filter(p -> {
                    if (!p.getPreAssyModuleType().getName().startsWith("(前置")) {
                        return false;
                    }

//                    String key = p.getModelName() + "_" + p.getPreAssyModuleType().getName();
//                    String moduleLinetype = p.getPreAssyModuleType().getLineType().getName();
//
//                    if (m3Linetype.contains(moduleLinetype) && mapM3WtInExcel.containsKey(key)) {
//                        p.setStandardTime(mapM3WtInExcel.get(key));
//                        return true;
//                    } else if (m6Linetype.contains(moduleLinetype) && mapM6WtInExcel.containsKey(key)) {
//                        p.setStandardTime(mapM6WtInExcel.get(key));
//                        return true;
//                    }
                    String key = p.getModelName() + "_"
                            + p.getPreAssyModuleType().getName() + "_"
                            + p.getPreAssyModuleType().getLineType().getName();
                    if (mapWtInExcel.containsKey(key)) {
                        BigDecimal newST = mapWtInExcel.get(key);
                        BigDecimal oldSt = p.getStandardTime();
                        BigDecimal avg = newST.add(oldSt).divide(new BigDecimal(2), 1, RoundingMode.HALF_UP);
                        p.setStandardTime(avg);
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
        HibernateObjectPrinter.print(ls);
//        preAssyModuleStandardTimeService.update(ls);
    }

//    private Map<String, BigDecimal> getPreAssyStandardTime(List<Map> data, List<String> floorName) {
    private Map<String, BigDecimal> getPreAssyStandardTime(List<Map> data) {
        Map<String, BigDecimal> mapSt = new HashMap<>();
        data.stream().filter(m
                -> //floorName.contains(m.get("sitefloor").toString())&&
                m.get("modelName") != null
                && m.get("preModuleName") != null
        )
                .collect(Collectors.groupingBy( //map -> map.get("modelName").toString() + "_" + map.get("preModuleName").toString()))
                        map
                        -> map.get("modelName").toString() + "_"
                        + map.get("preModuleName").toString() + "_"
                        + map.get("lineType").toString()
                ))
                .forEach((key, value) -> {
                    int pcs = value.stream().mapToInt(v -> (int) v.get("pcs")).sum();
                    int spend = value.stream().mapToInt(v -> (int) v.get("時間花費")).sum();
                    BigDecimal swt = new BigDecimal(spend).divide(new BigDecimal(pcs), 1, RoundingMode.HALF_UP);
                    mapSt.put(key, swt);
                });
        return mapSt;
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
    public void testListIntToString() {
        List<Bab> processingBabs = babService.findProcessing();

        List<String> stringList = processingBabs.stream().map(i -> i.getId())
                .map(Object::toString).collect(Collectors.toList());
        String input = String.join(",", stringList);
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
