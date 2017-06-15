package cn.buk.api.wechat.dto;

import java.util.List;

/**
 * Created by yfdai on 2017/2/24.
 */
public class WxMaterials extends BaseResponse {

    private int total_count;

    private int item_count;

    private List<WxMediaInfo> item;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public List<WxMediaInfo> getItem() {
        return item;
    }

    public void setItem(List<WxMediaInfo> item) {
        this.item = item;
    }

//    {
//        "total_count": TOTAL_COUNT,
//            "item_count": ITEM_COUNT,
//            "item": [{
//        "media_id": MEDIA_ID,
//                "name": NAME,
//                "update_time": UPDATE_TIME,
//                "url":URL
//    },
//        //可能会有多个素材
//  ]
//    }

}
