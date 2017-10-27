package com.arcaneless.ablmccapi;

import com.arcaneless.ablmccapi.types.Assignment;
import com.arcaneless.ablmccapi.types.ContactInfo;
import com.arcaneless.ablmccapi.types.GeneralInfo;
import com.arcaneless.ablmccapi.types.Notice;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CompletableFuture;


public class ABLMCCAPI {

    AsyncHttpClient client;

    public ABLMCCAPI() {
        client = new DefaultAsyncHttpClient();
    }

    public CompletableFuture<Response> getGeneralInfo(Callback<GeneralInfo> callback) {
        return client.prepareGet("http://web.ablmcc.edu.hk/Content/08_others/01_what_is_new/index.aspx")
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getMessage());
                    return null;
                })
                .thenApplyAsync(response -> {
                    Document doc = Jsoup.parse(response.getResponseBody());
                    callback.onCompleted(Finder.findInfo(doc));
                    return response;
                });
    }

    public CompletableFuture<Response> getNotice(Callback<Notice> callback, int year) {
        return getNotice(callback, year, "");
    }

    public CompletableFuture<Response> getNotice(Callback<Notice> callback, int year, String searchQuery) {
        return client.prepareGet("http://web.ablmcc.edu.hk/Content/07_parents/notice/index.aspx")
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getMessage());
                    return null;
                })
                .thenApplyAsync(response -> {
                    Document doc = Jsoup.parse(response.getResponseBody());
                    try {
                        String target = "ctl00$ContentPlaceHolder1$ddlstSchoolYear";
                        String search = "ctl00$ContentPlaceHolder1$txtSearch";
                        Date date = new Date();
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(date);

                        int yr = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int placeHolder = year == 0 ? (month < 9 ? yr - 1 : yr) : year;
                        client.preparePost("http://web.ablmcc.edu.hk/Content/07_parents/notice/index.aspx")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addFormParam("__EVENTTARGET", target)
                                .addFormParam("__VIEWSTATE", doc.getElementById("__VIEWSTATE").attr("value"))
                                .addFormParam("__SCROLLPOSITIONX", "0").addFormParam("__SCROLLPOSITIONY", "0")
                                .addFormParam("__EVENTVALIDATION", doc.getElementById("__EVENTVALIDATION").attr("value"))
                                .addFormParam(target, String.valueOf(placeHolder))
                                .addFormParam(search, searchQuery)
                                .execute().toCompletableFuture().exceptionally(e -> {
                            System.err.println(e.getMessage());
                            return null;
                        })
                                .thenApply(response1 -> {
                                    Document doc1 = Jsoup.parse(response1.getResponseBody());
                                    callback.onCompleted(Finder.findNotice(doc1));
                                    return response1;
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response;
                });
    }

    private int classNameConvert(String className) {
        if (className.length() == 2) {
            return (Character.getNumericValue(className.charAt(0)) - 1) * 4 + className.charAt(1) - 65;
        }
        return 0;
    }

    public CompletableFuture<Response> getAssignment(Callback<Assignment> callback, String className) {
        return client.prepareGet("http://web.ablmcc.edu.hk/Content/07_parents/homework/index.aspx")
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getMessage());
                    return null;
                })
                .thenApplyAsync(response -> {
                    Document doc = Jsoup.parse(response.getResponseBody());
                    String target = "ctl00$ContentPlaceHolder1$" + (204 + classNameConvert(className));
                    client.preparePost("http://web.ablmcc.edu.hk/Content/07_parents/homework/index.aspx")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addFormParam("__EVENTTARGET", target)
                            .addFormParam("__VIEWSTATE", doc.getElementById("__VIEWSTATE").attr("value"))
                            .addFormParam("__SCROLLPOSITIONX", "0").addFormParam("__SCROLLPOSITIONY", "0")
                            .addFormParam("__EVENTVALIDATION", doc.getElementById("__EVENTVALIDATION").attr("value"))
                            .execute().toCompletableFuture().exceptionally(e -> {
                        System.err.println(e.getMessage());
                        return null;
                    })
                            .thenApply(response1 -> {
                                Document doc1 = Jsoup.parse(response1.getResponseBody());
                                callback.onCompleted(Finder.findAssignment(doc1));
                                return response1;
                            });
                    return response;
                });
    }

    public CompletableFuture<Response> getContactInfo(Callback<ContactInfo> callback) {
        return client.prepareGet("http://web.ablmcc.edu.hk/CustomPage/26/content.html")
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getMessage());
                    return null;
                })
                .thenApplyAsync(response -> {
                    try {
                        Document doc = Jsoup.parse(new String(response.getResponseBodyAsBytes(), "Big5"));
                        doc.outputSettings().charset("Big5");
                        callback.onCompleted(Finder.findContactinfo(doc));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //System.out.println(doc.toString());
                    return response;
                });
    }

}
