package cqdatasdk.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HiFiMainPageBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4837760205239383242L;

//	{
//		code: "0",
//		data: [
//		{
//		name: "精选",
//		code: "jingxuan_2616",
//		id: 2616,
//		tuList: [
//		{
//		tu0: {
//		title: "2016湖南卫视元宵喜乐会",
//		href: "album.html?id=33",
//		id: 84330,
//		img: "2016/02/29/27b32954-da8c-4f16-a4be-33ecb5c82b58.png"
//		}
//		},
//		{},
//		{},
//		{
//		tu3: {
//		title: "Girl's Day《我想你》中文字幕",
//		href: "album.html?id=33",
//		id: 84335,
//		img: "2016/02/24/288d4e2a-a162-4cc5-b785-50126c003a53.png"
//		}
//		},
//		{},
//		{
//		tu5: {
//		title: "徐佳莹《大雨将至》",
//		href: "album.html?id=33",
//		id: 84336,
//		img: "2016/02/24/842a6e09-8a8b-4144-9d92-e4cdcfdf3d1c.png"
//		}
//		},
//		{},
//		{
//		tu7: {
//		title: "张碧晨《幸福梦》",
//		href: "tvutvgo/tvPage/ks_detailNew.jsp?&channelId=10052&contentId=31855&fromTable=hc",
//		id: 84338,
//		img: "2016/02/24/898187ef-e674-4e6e-a88f-7935dd253c00.png"
//		}
//		},
//		{
//		tu8: {
//		title: "李克勤《月半小夜曲》演唱会",
//		href: "recordList.html?menuPos=1&subMenuPos=0",
//		id: 84339,
//		img: "2016/02/24/dd07abdf-c135-496a-b3e0-564171b5fc2d.png"
//		}
//		}
//		]
//		},
//		{},
//		{},
//		{},
//		{},
//		{},
//		{}
//		]
//		}

    public String code;//状态码
    public String msg;
    public List<ClassifyBean> data;//用于存储object类对象

    public class ClassifyBean implements Serializable {
        public String name;
        public String code;//code: "jingxuan_2616",
        public int id;
        public List<Map<String, ClassifyItemBean>> tuList;//存储map对象

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ClassifyBean other = (ClassifyBean) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (id != other.id)
                return false;
            return true;
        }


        @Override
        public String toString() {
            return "ClassifyBean [name=" + name + ", code=" + code + ", id=" + id
                    + ", tuList=" + tuList + "]";
        }

        public class ClassifyItemBean implements Serializable {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public String title;
            public String href;
            public int id;
            public String img;
            public int linkType;

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("ClassifyItemBean{");
                sb.append("title='").append(title).append('\'');
                sb.append(", href='").append(href).append('\'');
                sb.append(", id=").append(id);
                sb.append(", img='").append(img).append('\'');
                sb.append(", linkType=").append(linkType);
                sb.append('}');
                return sb.toString();
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + getOuterType().hashCode();
                result = prime * result + id;
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                ClassifyItemBean other = (ClassifyItemBean) obj;
                if (!getOuterType().equals(other.getOuterType()))
                    return false;
                if (id != other.id)
                    return false;
                return true;
            }

            private ClassifyBean getOuterType() {

                return ClassifyBean.this;
            }

        }

        private HiFiMainPageBean getOuterType() {
            return HiFiMainPageBean.this;
        }

    }


}
