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

        @SerializedName("short_description")
        @Expose
        private String shortDescription;

        @SerializedName("long_description")
        @Expose
        private String longDescription;

        @SerializedName("pic")
        @Expose
        private String pic;

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        @Override
        public String toString() {
            return "\nshortDescription: " + shortDescription /*+ "\nlongDescription: " + longDescription*/ + "\npic: " + pic + "\n";
        }
    }
}
