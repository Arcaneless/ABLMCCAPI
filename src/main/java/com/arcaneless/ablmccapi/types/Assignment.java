package com.arcaneless.ablmccapi.types;

import com.arcaneless.ablmccapi.Utils;

import java.util.ArrayList;
import java.util.List;

public class Assignment {

    public enum Day {
        TODAY,
        NEXTDAY,
        NEXTNEXTDAY
    }

    private List<CompoundBasic> triList;

    public Assignment() {
        triList = new ArrayList<>();
        triList.add(new CompoundBasic());
        triList.add(new CompoundBasic());
        triList.add(new CompoundBasic());
    }

    public CompoundBasic getToday() {
        return triList.get(Day.TODAY.ordinal());
    }

    public CompoundBasic getNextDay() {
        return triList.get(Day.NEXTDAY.ordinal());
    }

    public CompoundBasic getNextNextDay() {
        return triList.get(Day.NEXTNEXTDAY.ordinal());
    }

    public void setList(Day day, CompoundBasic basic) {
        triList.set(day.ordinal(), basic);
    }

    @Override
    public String toString() {
        String result = "[";
        for (CompoundBasic i : triList) {
            result = result.concat(Day.values()[triList.indexOf(i)].toString() + ": ");
            result = result.concat(i.toString() + (Utils.endOf(triList, i) ? "" : "\n"));
        }
        result = result.concat("]");
        return result;
    }

    public static class CompoundBasic {
        private String title;
        private List<Basic> basic;

        public CompoundBasic() {
            basic = new ArrayList<>();
            title = "";
        }

        public List<Basic> getList() {
            return basic;
        }

        public CompoundBasic setTitle(String newTitle) {
            title = newTitle;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public void append(Basic input) {
            basic.add(input);
        }

        @Override
        public String toString() {
            String result = "[" + getTitle() + " ";
            for (Basic i : basic) {
                result = result.concat(i.toString() + (Utils.endOf(basic, i) ? "" : "\n"));
            }
            result = result.concat("]");
            return result;
        }
    }


    public static class Basic {
        private String subject, date, summary;

        public Basic(String subject, String date, String summary) {
            this.subject = subject;
            this.date = date;
            this.summary = summary;
        }

        public String getSubject() {
            return subject;
        }

        public String getDate() {
            return date;
        }

        public String getSummary() {
            return summary;
        }

        @Override
        public String toString() {
            return "{\'subject\': " + subject + ", \'date\': " + date + ", \'summary\': " + summary + "}";
        }
    }
}


