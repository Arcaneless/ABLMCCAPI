package com.arcaneless.ablmccapi.types;

import com.arcaneless.ablmccapi.Utils;

import java.util.ArrayList;
import java.util.List;

public class Notice {

    private List<Info> infos;

    public Notice() {
        infos = new ArrayList<>();
    }

    public List<Info> getList() {
        return infos;
    }

    public void append(Info data) {
        infos.add(data);
    }

    public int size() {
        return infos.size();
    }

    @Override
    public String toString() {
        String result = "[";
        for (Info i : infos) {
            result = result.concat(i.toString() + (Utils.endOf(infos, i) ? "" : "\n"));
        }
        result = result.concat("]");
        return result;
    }

}
