package com.xiaomi.videonews.bombapi.entity;

/**
 * 作者：yuanchao on 2016/8/16 0016 11:48
 * 邮箱：yuanchao@feicuiedu.com
 */
public class NewsEntity extends BaseEntity {

    private String newsTitle; // 新闻标题

    private String videoUrl; // 视频地址

    private String previewUrl; // 视频预览图地址

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

//    {
//                "objectId":新闻id,
//                "newsTitle":新闻标题,
//                "videoUrl":视频地址,
//                "previewUrl":预览图地址,
//                "createAt":创建日期,
//                "updateAt":更新日期
//        },
}
