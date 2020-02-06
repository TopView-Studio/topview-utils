package com.topview.utils.oss.qiniu;

import com.qiniu.storage.Region;

/**
 * @author Albumen
 * @date 2020/1/29
 */
public enum QiniuRegionEnum {
    AUTO(-1),
    HUA_DONG(0),
    HUA_BEI(1),
    HUA_NAN(2),
    BEI_MEI(3),
    XIN_JIA_PO(4);

    private int code;

    QiniuRegionEnum(int code) {
        this.code = code;
    }

    public static QiniuRegionEnum getRegionEnum(String region) {
        switch (region) {
            case "HUA_DONG":
                return HUA_DONG;
            case "HUA_BEI":
                return HUA_BEI;
            case "HUA_NAN":
                return HUA_NAN;
            case "BEI_MEI":
                return BEI_MEI;
            case "XIN_JIA_PO":
                return XIN_JIA_PO;
            default:
                return AUTO;
        }
    }

    public Region getRegion() {
        switch (code) {
            case 0:
                return Region.huadong();
            case 1:
                return Region.huabei();
            case 2:
                return Region.huanan();
            case 3:
                return Region.beimei();
            case 4:
                return Region.xinjiapo();
            default:
                return Region.autoRegion();
        }
    }

    public String getName() {
        switch (code) {
            case 0:
                return "HUA_DONG";
            case 1:
                return "HUA_BEI";
            case 2:
                return "HUA_NAN";
            case 3:
                return "BEI_MEI";
            case 4:
                return "XIN_JIA_PO";
            default:
                return "AUTO";
        }
    }
}
