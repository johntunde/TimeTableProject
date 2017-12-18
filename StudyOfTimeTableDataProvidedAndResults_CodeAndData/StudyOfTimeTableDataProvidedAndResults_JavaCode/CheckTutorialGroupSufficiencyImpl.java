
import java.util.ArrayList;

/**
 * Project: Time Table Project
 * Date: 18 December 2017
 * Version: 1.0
 * Programmers: ZAHID HUSSAIN, JOHN OLORUNSUYI, Eric Chiu
 * Course: MSc Computer Science (CN7021), University of East London
 */
public class CheckTutorialGroupSufficiencyImpl implements CheckTutorialGroupSufficiency {


    //@Override
    public ArrayList<String> checkTutorialGroupSufficiency(ArrayList<ArrayList<String>> firstList) {

        FormModuleList fml = new FormModuleListImpl();
        ArrayList<ModuleImpl> moduleCodes = new ArrayList<>(fml.moduleCodes());
        ArrayList<ModuleImpl> moduleCodes1 = fml.addingDataToModuleCodes(firstList, moduleCodes);
        ArrayList<String> tutorialGroupSufficencyResult=new ArrayList<>();
        String firstLine = "Find whether tutorial/practical groups set for each module need to be increased " +
                "or reduced and for how many:";
        String secondLine= "(Maximum number of students per group: 25 / " +
                "Room capacity having not been taken into consideration)\n";
        String warning ="WARNING:  Not able to study these modules since " +
                "they contained only in either the timetable or the module list:";
        String list1= new CmprTmTblNModLstTrimModLstNProdWrnngImpl().modulesNotInModuleList
                (new InputAndTidyUpModuleListImpl().modStudNumList(),
                        new InputAndTidyUpTimeTableImpl().
                                inputAndTidyTimeTable())
                .toString().replace("[","").replace("]","").replace(",","/");
        String list2= new CmprTmTblNModLstTrimModLstNProdWrnngImpl().modulesNotInTimeTable
                (new InputAndTidyUpModuleListImpl().modStudNumList(),
                        new InputAndTidyUpTimeTableImpl().
                                inputAndTidyTimeTable())
                .toString().replace("[","").replace("]","").replace(",","/");
        System.out.println();
        System.out.println(firstLine);
        System.out.println(secondLine);
        System.out.println(warning);
        System.out.println(" "+list1);
        System.out.println(" "+list2);
        tutorialGroupSufficencyResult.add("");
        tutorialGroupSufficencyResult.add(firstLine);
        tutorialGroupSufficencyResult.add(secondLine);
        tutorialGroupSufficencyResult.add(warning);
        tutorialGroupSufficencyResult.add(" "+list1);
        tutorialGroupSufficencyResult.add(" "+list2);

        for (ModuleImpl m : moduleCodes) {
            int count = 0;
            for (ArrayList<String> as : m.getTypeOfMeetingNRmNumList()) {
                if (as.get(0).trim().toLowerCase().contains("tutorial") ||
                        as.get(0).trim().toLowerCase().contains("practical")) {
                    count++;
                }
            }
            if (count == 0) {
                String line = m.getCourseCode()+" This module does not have tutorial/practical or " +
                        "lecture and tutorial/practical are combined.";
                System.out.println(line);
                tutorialGroupSufficencyResult.add(line);
            }
            else if (m.getStudentNum() > 25 * count) {
                int additionalGroupNum = (int) Math.ceil(m.getStudentNum() / 25d) - count;
                String line = m.getCourseCode()+",Studt No,"+m.getStudentNum()+",Grp No,"+
                        count+",Req +Grp,"+additionalGroupNum;
                System.out.println(line);
                tutorialGroupSufficencyResult.add(line);
            }
            else if (m.getStudentNum() <= 25 * count && m.getStudentNum() > 25 * (count - 1)) {
                String line = m.getCourseCode()+",Studt No,"+m.getStudentNum()+",Grp No,"+
                        count+",OK";
                System.out.println(line);
                tutorialGroupSufficencyResult.add(line);
            }
            else {
                int reducingGroupNum = count - (int) Math.ceil(m.getStudentNum() / 25d);
                String line = m.getCourseCode()+",Studt No,"+m.getStudentNum()+",Grp No,"+
                        count+",Req -Grp,"+reducingGroupNum;
                System.out.println(line);
                tutorialGroupSufficencyResult.add(line);
            }
        }
        return tutorialGroupSufficencyResult;
    }
}