package com.amt.code.plugin.inspect

class Config {
    // 第三方库白名单
    public String[] whiteList = []

    void whiteList(String[] whiteList) {
        this.whiteList = whiteList
    }

    String toString() {
        StringBuilder result = new StringBuilder();
        result.append("<<<<<<<<<<<<<<AmtTpLibraryConfig>>>>>>>>>>>>" + "\n");
        result.append("whiteList: \n");
        for(String file : whiteList) {
            result.append(file + "\n");
        }
        result.append("<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>");
        return result.toString();
    }
}