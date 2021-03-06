package com.arcaneless.ablmccapi;

import com.arcaneless.ablmccapi.types.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

// NO DOC
public class Finder {

    protected static GeneralInfo findInfo(Document doc) {
        GeneralInfo info = new GeneralInfo();
        Elements table = doc.getElementsByClass("latestNews").get(0).children().get(0).children();
        table.forEach(e -> {
            String date = e.getElementsByClass("date").get(0).text();
            String text = e.getElementsByClass("caption").get(0).text().split(" ")[0];
            Elements ele = e.getElementsByClass("fileCaption");
            ele.forEach(x -> {
                String t = x.getAllElements().get(0).text();
                String href = x.child(0).attr("href").replace("../../..", "http://web.ablmcc.edu.hk");
                if (ele.size() > 1) t = text + " " + t;
                else t = text;
                info.append(new Info(t, date, href));
            });
        });
        return info;
    }

    protected static Notice findNotice(Document doc) {
        Notice notice = new Notice();
        Elements table = doc.getElementsByClass("noticeList");
        if (table.size() != 0) {
            table = table.get(0).children().get(0).children();
            table.remove(0);
            table.forEach(e -> {
                String date = e.getElementsByClass("date").get(0).text();
                String text = e.getElementsByClass("caption").get(0).text();
                String href = e.getElementsByTag("a").get(0).attr("href").replace("../../..", "http://web.ablmcc.edu.hk");
                notice.append(new Info(text, date, href));
            });
        }
        return notice;
    }

    private static Assignment.CompoundBasic buildAssignment(Element table) {

        Assignment.CompoundBasic basics = new Assignment.CompoundBasic();
        Elements ele = table.child(0).children();
        ele.removeIf(f -> f.hasClass("heading"));
        ele.forEach(e -> {
            Elements sub = e.getElementsByClass("subject");
            String subject = sub.size() > 0 ? sub.get(0).text() : basics.getList().get(basics.getList().size() - 1).getSubject();
            Elements s = e.getElementsByClass("homeworkHandInDate");
            if (s.size() == 0) s = e.getElementsByClass("homeworkIssueDate");
            String date = s.get(0).text();
            String text = e.getElementsByClass("homeworkText").get(0).text();
            basics.append(new Assignment.Basic(subject, date, text));
        });
        return basics;
    }

    protected static Assignment findAssignment(Document doc) {
        Assignment ass = new Assignment();
        Elements e = doc.getElementsByClass("homeworkDue");
        Elements ee = doc.getElementsByClass("homeworkIssue");
        if (ee.size() > 0)
            ass.setList(Assignment.Day.TODAY, buildAssignment(ee.get(0)).setTitle(doc.getElementsByClass("issueDateHeading").get(0).text()));
        else ass.setList(Assignment.Day.TODAY, new Assignment.CompoundBasic());
        if (e.size() > 0)
            ass.setList(Assignment.Day.NEXTDAY, buildAssignment(e.get(0)).setTitle(doc.getElementsByClass("dueTableHeading").get(0).text()));
        else ass.setList(Assignment.Day.NEXTDAY, new Assignment.CompoundBasic());
        if (e.size() > 1)
            ass.setList(Assignment.Day.NEXTNEXTDAY, buildAssignment(e.get(1)).setTitle(doc.getElementsByClass("dueTableHeading").get(1).text()));
        else ass.setList(Assignment.Day.NEXTNEXTDAY, new Assignment.CompoundBasic());
        return ass;
    }

    // HTML Each Category request
    private static ContactInfo.ContactCategory buildCategory(String category, String link)
            throws IOException {
        // Structure build
        ContactInfo.ContactCategory cc = new ContactInfo.ContactCategory(category);

        Document doc = Jsoup.connect(link).get();
        // document processing
        // Find all column
        Elements list = doc.getElementsByTag("tr");
        // Remove the annoying top bar
        if (list.size() == 0) return cc;
        list.remove(0);

        // Start iterating column
        list.forEach(e -> {
            if (e.getElementsByTag("b").size() == 0) return;
            String name = e.getElementsByTag("b").get(0).text();
            String email = e.getElementsByAttributeValueContaining("href", "mailto").text().replace("mailto:", "");

            // Adding to list
            cc.add(new ContactInfo.Contact(name, email));
        });
        return cc;
    }

    protected static ContactInfo findContactinfo(Document doc) {
        ContactInfo contact = new ContactInfo();
        Elements e = doc.getElementsByAttributeValueContaining("href", "http://web.ablmcc.edu.hk/CustomPage/26/subjects/");
        try {
            for (Element element : e) {
                // new String(element.text().getBytes("Big5"), "UTF-8")
                contact.addCategory(buildCategory(element.text(), element.attr("href")));
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
        return contact;
    }

}
