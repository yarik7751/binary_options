package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ярослав Левшунов on 24.02.2017.
 */

public class BinaryOptionAnswer {

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("num_pages")
    @Expose
    private Integer numPages;

    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("results")
    @Expose
    private ArrayList<Element> elements = new ArrayList<>();

    private static BinaryOptionAnswer binatyOptionAnswer = null;
    public static void setInstance(BinaryOptionAnswer instance) {
        binatyOptionAnswer = instance;
    }
    public static BinaryOptionAnswer getInstance() {
        return binatyOptionAnswer;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNumPages() {
        return numPages;
    }

    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "count: " + count + ", numPages: " + numPages + ", page: " + page;
    }

    public class Element {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("slogan")
        @Expose
        private String slogan;
        @SerializedName("slogan_ru")
        @Expose
        private String sloganRu;
        @SerializedName("slogan_en")
        @Expose
        private String sloganEn;
        @SerializedName("slogan_zh_cn")
        @Expose
        private String sloganZhCn;
        @SerializedName("slogan_id")
        @Expose
        private String sloganId;
        @SerializedName("slogan_pl")
        @Expose
        private String sloganPl;
        @SerializedName("slogan_pt")
        @Expose
        private String sloganPt;
        @SerializedName("slogan_ar")
        @Expose
        private String sloganAr;
        @SerializedName("slogan_uk")
        @Expose
        private String sloganUk;
        @SerializedName("slogan_fa")
        @Expose
        private String sloganFa;
        @SerializedName("slogan_hy")
        @Expose
        private String sloganHy;
        @SerializedName("slogan_ka")
        @Expose
        private String sloganKa;
        @SerializedName("slogan_fr")
        @Expose
        private String sloganFr;
        @SerializedName("slogan_th")
        @Expose
        private String sloganTh;
        @SerializedName("slogan_vi")
        @Expose
        private String sloganVi;
        @SerializedName("slogan_ms")
        @Expose
        private String sloganMs;
        @SerializedName("short_description")
        @Expose
        private String shortDescription;
        @SerializedName("short_description_ru")
        @Expose
        private String shortDescriptionRu;
        @SerializedName("short_description_en")
        @Expose
        private String shortDescriptionEn;
        @SerializedName("short_description_zh_cn")
        @Expose
        private String shortDescriptionZhCn;
        @SerializedName("short_description_id")
        @Expose
        private String shortDescriptionId;
        @SerializedName("short_description_pl")
        @Expose
        private String shortDescriptionPl;
        @SerializedName("short_description_pt")
        @Expose
        private String shortDescriptionPt;
        @SerializedName("short_description_ar")
        @Expose
        private String shortDescriptionAr;
        @SerializedName("short_description_uk")
        @Expose
        private String shortDescriptionUk;
        @SerializedName("short_description_fa")
        @Expose
        private String shortDescriptionFa;
        @SerializedName("short_description_hy")
        @Expose
        private String shortDescriptionHy;
        @SerializedName("short_description_ka")
        @Expose
        private String shortDescriptionKa;
        @SerializedName("short_description_fr")
        @Expose
        private String shortDescriptionFr;
        @SerializedName("short_description_th")
        @Expose
        private String shortDescriptionTh;
        @SerializedName("short_description_vi")
        @Expose
        private String shortDescriptionVi;
        @SerializedName("short_description_ms")
        @Expose
        private String shortDescriptionMs;
        @SerializedName("long_description")
        @Expose
        private String longDescription;
        @SerializedName("long_description_ru")
        @Expose
        private String longDescriptionRu;
        @SerializedName("long_description_en")
        @Expose
        private String longDescriptionEn;
        @SerializedName("long_description_zh_cn")
        @Expose
        private String longDescriptionZhCn;
        @SerializedName("long_description_id")
        @Expose
        private String longDescriptionId;
        @SerializedName("long_description_pl")
        @Expose
        private String longDescriptionPl;
        @SerializedName("long_description_pt")
        @Expose
        private String longDescriptionPt;
        @SerializedName("long_description_ar")
        @Expose
        private String longDescriptionAr;
        @SerializedName("long_description_uk")
        @Expose
        private String longDescriptionUk;
        @SerializedName("long_description_fa")
        @Expose
        private String longDescriptionFa;
        @SerializedName("long_description_hy")
        @Expose
        private String longDescriptionHy;
        @SerializedName("long_description_ka")
        @Expose
        private String longDescriptionKa;
        @SerializedName("long_description_fr")
        @Expose
        private String longDescriptionFr;
        @SerializedName("long_description_th")
        @Expose
        private String longDescriptionTh;
        @SerializedName("long_description_vi")
        @Expose
        private String longDescriptionVi;
        @SerializedName("long_description_ms")
        @Expose
        private String longDescriptionMs;
        @SerializedName("hide_from_private_office")
        @Expose
        private Boolean hideFromPrivateOffice;
        @SerializedName("pic")
        @Expose
        private String pic;
        @SerializedName("start")
        @Expose
        private String start;
        @SerializedName("end")
        @Expose
        private String end;

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public Boolean getHideFromPrivateOffice() {
            return hideFromPrivateOffice;
        }

        public void setHideFromPrivateOffice(Boolean hideFromPrivateOffice) {
            this.hideFromPrivateOffice = hideFromPrivateOffice;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }

        public String getLongDescriptionAr() {
            return longDescriptionAr;
        }

        public void setLongDescriptionAr(String longDescriptionAr) {
            this.longDescriptionAr = longDescriptionAr;
        }

        public String getLongDescriptionEn() {
            return longDescriptionEn;
        }

        public void setLongDescriptionEn(String longDescriptionEn) {
            this.longDescriptionEn = longDescriptionEn;
        }

        public String getLongDescriptionFa() {
            return longDescriptionFa;
        }

        public void setLongDescriptionFa(String longDescriptionFa) {
            this.longDescriptionFa = longDescriptionFa;
        }

        public String getLongDescriptionFr() {
            return longDescriptionFr;
        }

        public void setLongDescriptionFr(String longDescriptionFr) {
            this.longDescriptionFr = longDescriptionFr;
        }

        public String getLongDescriptionHy() {
            return longDescriptionHy;
        }

        public void setLongDescriptionHy(String longDescriptionHy) {
            this.longDescriptionHy = longDescriptionHy;
        }

        public String getLongDescriptionId() {
            return longDescriptionId;
        }

        public void setLongDescriptionId(String longDescriptionId) {
            this.longDescriptionId = longDescriptionId;
        }

        public String getLongDescriptionKa() {
            return longDescriptionKa;
        }

        public void setLongDescriptionKa(String longDescriptionKa) {
            this.longDescriptionKa = longDescriptionKa;
        }

        public String getLongDescriptionMs() {
            return longDescriptionMs;
        }

        public void setLongDescriptionMs(String longDescriptionMs) {
            this.longDescriptionMs = longDescriptionMs;
        }

        public String getLongDescriptionPl() {
            return longDescriptionPl;
        }

        public void setLongDescriptionPl(String longDescriptionPl) {
            this.longDescriptionPl = longDescriptionPl;
        }

        public String getLongDescriptionPt() {
            return longDescriptionPt;
        }

        public void setLongDescriptionPt(String longDescriptionPt) {
            this.longDescriptionPt = longDescriptionPt;
        }

        public String getLongDescriptionRu() {
            return longDescriptionRu;
        }

        public void setLongDescriptionRu(String longDescriptionRu) {
            this.longDescriptionRu = longDescriptionRu;
        }

        public String getLongDescriptionTh() {
            return longDescriptionTh;
        }

        public void setLongDescriptionTh(String longDescriptionTh) {
            this.longDescriptionTh = longDescriptionTh;
        }

        public String getLongDescriptionUk() {
            return longDescriptionUk;
        }

        public void setLongDescriptionUk(String longDescriptionUk) {
            this.longDescriptionUk = longDescriptionUk;
        }

        public String getLongDescriptionVi() {
            return longDescriptionVi;
        }

        public void setLongDescriptionVi(String longDescriptionVi) {
            this.longDescriptionVi = longDescriptionVi;
        }

        public String getLongDescriptionZhCn() {
            return longDescriptionZhCn;
        }

        public void setLongDescriptionZhCn(String longDescriptionZhCn) {
            this.longDescriptionZhCn = longDescriptionZhCn;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getShortDescriptionAr() {
            return shortDescriptionAr;
        }

        public void setShortDescriptionAr(String shortDescriptionAr) {
            this.shortDescriptionAr = shortDescriptionAr;
        }

        public String getShortDescriptionEn() {
            return shortDescriptionEn;
        }

        public void setShortDescriptionEn(String shortDescriptionEn) {
            this.shortDescriptionEn = shortDescriptionEn;
        }

        public String getShortDescriptionFa() {
            return shortDescriptionFa;
        }

        public void setShortDescriptionFa(String shortDescriptionFa) {
            this.shortDescriptionFa = shortDescriptionFa;
        }

        public String getShortDescriptionFr() {
            return shortDescriptionFr;
        }

        public void setShortDescriptionFr(String shortDescriptionFr) {
            this.shortDescriptionFr = shortDescriptionFr;
        }

        public String getShortDescriptionHy() {
            return shortDescriptionHy;
        }

        public void setShortDescriptionHy(String shortDescriptionHy) {
            this.shortDescriptionHy = shortDescriptionHy;
        }

        public String getShortDescriptionId() {
            return shortDescriptionId;
        }

        public void setShortDescriptionId(String shortDescriptionId) {
            this.shortDescriptionId = shortDescriptionId;
        }

        public String getShortDescriptionKa() {
            return shortDescriptionKa;
        }

        public void setShortDescriptionKa(String shortDescriptionKa) {
            this.shortDescriptionKa = shortDescriptionKa;
        }

        public String getShortDescriptionMs() {
            return shortDescriptionMs;
        }

        public void setShortDescriptionMs(String shortDescriptionMs) {
            this.shortDescriptionMs = shortDescriptionMs;
        }

        public String getShortDescriptionPl() {
            return shortDescriptionPl;
        }

        public void setShortDescriptionPl(String shortDescriptionPl) {
            this.shortDescriptionPl = shortDescriptionPl;
        }

        public String getShortDescriptionPt() {
            return shortDescriptionPt;
        }

        public void setShortDescriptionPt(String shortDescriptionPt) {
            this.shortDescriptionPt = shortDescriptionPt;
        }

        public String getShortDescriptionRu() {
            return shortDescriptionRu;
        }

        public void setShortDescriptionRu(String shortDescriptionRu) {
            this.shortDescriptionRu = shortDescriptionRu;
        }

        public String getShortDescriptionTh() {
            return shortDescriptionTh;
        }

        public void setShortDescriptionTh(String shortDescriptionTh) {
            this.shortDescriptionTh = shortDescriptionTh;
        }

        public String getShortDescriptionUk() {
            return shortDescriptionUk;
        }

        public void setShortDescriptionUk(String shortDescriptionUk) {
            this.shortDescriptionUk = shortDescriptionUk;
        }

        public String getShortDescriptionVi() {
            return shortDescriptionVi;
        }

        public void setShortDescriptionVi(String shortDescriptionVi) {
            this.shortDescriptionVi = shortDescriptionVi;
        }

        public String getShortDescriptionZhCn() {
            return shortDescriptionZhCn;
        }

        public void setShortDescriptionZhCn(String shortDescriptionZhCn) {
            this.shortDescriptionZhCn = shortDescriptionZhCn;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
        }

        public String getSloganAr() {
            return sloganAr;
        }

        public void setSloganAr(String sloganAr) {
            this.sloganAr = sloganAr;
        }

        public String getSloganEn() {
            return sloganEn;
        }

        public void setSloganEn(String sloganEn) {
            this.sloganEn = sloganEn;
        }

        public String getSloganFa() {
            return sloganFa;
        }

        public void setSloganFa(String sloganFa) {
            this.sloganFa = sloganFa;
        }

        public String getSloganFr() {
            return sloganFr;
        }

        public void setSloganFr(String sloganFr) {
            this.sloganFr = sloganFr;
        }

        public String getSloganHy() {
            return sloganHy;
        }

        public void setSloganHy(String sloganHy) {
            this.sloganHy = sloganHy;
        }

        public String getSloganId() {
            return sloganId;
        }

        public void setSloganId(String sloganId) {
            this.sloganId = sloganId;
        }

        public String getSloganKa() {
            return sloganKa;
        }

        public void setSloganKa(String sloganKa) {
            this.sloganKa = sloganKa;
        }

        public String getSloganMs() {
            return sloganMs;
        }

        public void setSloganMs(String sloganMs) {
            this.sloganMs = sloganMs;
        }

        public String getSloganPl() {
            return sloganPl;
        }

        public void setSloganPl(String sloganPl) {
            this.sloganPl = sloganPl;
        }

        public String getSloganPt() {
            return sloganPt;
        }

        public void setSloganPt(String sloganPt) {
            this.sloganPt = sloganPt;
        }

        public String getSloganRu() {
            return sloganRu;
        }

        public void setSloganRu(String sloganRu) {
            this.sloganRu = sloganRu;
        }

        public String getSloganTh() {
            return sloganTh;
        }

        public void setSloganTh(String sloganTh) {
            this.sloganTh = sloganTh;
        }

        public String getSloganUk() {
            return sloganUk;
        }

        public void setSloganUk(String sloganUk) {
            this.sloganUk = sloganUk;
        }

        public String getSloganVi() {
            return sloganVi;
        }

        public void setSloganVi(String sloganVi) {
            this.sloganVi = sloganVi;
        }

        public String getSloganZhCn() {
            return sloganZhCn;
        }

        public void setSloganZhCn(String sloganZhCn) {
            this.sloganZhCn = sloganZhCn;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        @Override
        public String toString() {
            return "\nshortDescription: " + shortDescription /*+ "\nlongDescription: " + longDescription*/ + "\npic: " + pic + "\n";
        }
    }
}
