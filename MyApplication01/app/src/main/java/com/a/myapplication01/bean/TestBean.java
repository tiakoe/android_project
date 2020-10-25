package com.a.myapplication01.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TestBean implements Parcelable {


    /**
     * result : {"msg":"success","code":200}
     * data : {"entries":[{"explain":"art. 一；任一；每一","entry":"a"},{"explain":"conj.
     * 和，与；就；而且；但是；然后; n. (And)人名；(土、瑞典)安德","entry":"and"},{"explain":"adv. 如同，像\u2026\u2026一样;
     * prep. 作为；以\u2026\u2026身份；当作；像，如同；当\u2026\u2026时; conj. ...","entry":"as"},{"explain":"art.
     * 一（在元音音素前）","entry":"an"},{"explain":"prep. 在（表示存在或出现的地点、场所、位置、空间）；在\u2026\u2026岁时；在\u2026
     * \u2026远；以（某种价格、速度等...","entry":"at"},{"explain":"v. 是（be的第二人称单复数现在式）; n. 公亩; n. (Are)人名；
     * (意、西、芬)阿雷","entry":"are"},{"explain":"abbr. 汽车协会（Automobile Association）；嗜酒者互诫协会（Alcohol..
     * .","entry":"aa"},{"explain":"adj. 可获得的；可购得的；可找到的；有空的","entry":"available"},{"explain
     * ":"abbr. 调频，调幅 (amplitude modulation); v. 是（be 的第一人称单...","entry":"am"},{"explain":"n.
     * 分析；分解；验定","entry":"analysis"},{"explain":"n. 地址；所在地；位置储存编码；演讲；称呼；致辞；谈吐；技巧; v.
     * 写（收信人）姓名地址；演说；...","entry":"address"},{"explain":"adj. 适当的；恰当的；合适的; vt. 占用，拨出",
     * "entry":"appropriate"},{"explain":"n. 通道；进入；机会；使用权；探望权；（对计算机存储器的）访问；（情感）爆发; v. 接近，使用；...",
     * "entry":"access"},{"explain":"n. 账户；解释；账目，账单；理由；描述; vi. 解释；导致；报账; vt. 认为；把\u2026视为",
     * "entry":"account"},{"explain":"v. 走进；与\u2026\u2026接洽；处理；临近，逐渐接近（某时间或事件）；几乎达到（某水平或状态）; n.
     * 方法...","entry":"approach"}],"query":"a","language":"eng","type":"dict"}
     */

    private ResultBean result;
    private DataBean data;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class ResultBean {
        /**
         * msg : success
         * code : 200
         */

        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static class DataBean {
        /**
         * entries : [{"explain":"art. 一；任一；每一","entry":"a"},{"explain":"conj. 和，与；就；而且；但是；然后; n.
         * (And)人名；(土、瑞典)安德","entry":"and"},{"explain":"adv. 如同，像\u2026\u2026一样; prep.
         * 作为；以\u2026\u2026身份；当作；像，如同；当\u2026\u2026时; conj. ...","entry":"as"},{"explain":"art.
         * 一（在元音音素前）","entry":"an"},{"explain":"prep. 在（表示存在或出现的地点、场所、位置、空间）；在\u2026\u2026
         * 岁时；在\u2026\u2026远；以（某种价格、速度等...","entry":"at"},{"explain":"v. 是（be的第二人称单复数现在式）; n. 公亩;
         * n. (Are)人名；(意、西、芬)阿雷","entry":"are"},{"explain":"abbr. 汽车协会（Automobile
         * Association）；嗜酒者互诫协会（Alcohol...","entry":"aa"},{"explain":"adj. 可获得的；可购得的；可找到的；有空的",
         * "entry":"available"},{"explain":"abbr. 调频，调幅 (amplitude modulation); v. 是（be 的第一人称单..
         * .","entry":"am"},{"explain":"n. 分析；分解；验定","entry":"analysis"},{"explain":"n.
         * 地址；所在地；位置储存编码；演讲；称呼；致辞；谈吐；技巧; v. 写（收信人）姓名地址；演说；...","entry":"address"},{"explain":"adj
         * . 适当的；恰当的；合适的; vt. 占用，拨出","entry":"appropriate"},{"explain":"n.
         * 通道；进入；机会；使用权；探望权；（对计算机存储器的）访问；（情感）爆发; v. 接近，使用；...","entry":"access"},{"explain":"n.
         * 账户；解释；账目，账单；理由；描述; vi. 解释；导致；报账; vt. 认为；把\u2026视为","entry":"account"},{"explain":"v.
         * 走进；与\u2026\u2026接洽；处理；临近，逐渐接近（某时间或事件）；几乎达到（某水平或状态）; n. 方法...","entry":"approach"}]
         * query : a
         * language : eng
         * type : dict
         */

        private String query;
        private String language;
        private String type;
        private List<EntriesBean> entries;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<EntriesBean> getEntries() {
            return entries;
        }

        public void setEntries(List<EntriesBean> entries) {
            this.entries = entries;
        }

        public static class EntriesBean {
            /**
             * explain : art. 一；任一；每一
             * entry : a
             */

            private String explain;
            private String entry;

            public String getExplain() {
                return explain;
            }

            public void setExplain(String explain) {
                this.explain = explain;
            }

            public String getEntry() {
                return entry;
            }

            public void setEntry(String entry) {
                this.entry = entry;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) this.result, flags);
        dest.writeParcelable((Parcelable) this.data, flags);
    }

    public TestBean() {
    }

    protected TestBean(Parcel in) {
        this.result = in.readParcelable(ResultBean.class.getClassLoader());
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<TestBean> CREATOR = new Parcelable.Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel source) {
            return new TestBean(source);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };
}
