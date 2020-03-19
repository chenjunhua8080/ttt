package com.cjh.ttt.base.map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地图dto
 *
 * @author cjh
 * @date 2020/3/19
 */
@NoArgsConstructor
@Data
public class MapDto {

    private Integer status;
    private String message;
    private String requestId;
    private ResultBean result;

    @NoArgsConstructor
    @Data
    public static class ResultBean {

        private LocationBean location;
        private String address;
        private FormattedAddressesBean formattedAddresses;
        private AddressComponentBean addressComponent;
        private AdInfoBean adInfo;

        @NoArgsConstructor
        @Data
        public static class LocationBean {

            private String lat;
            private String lng;
        }

        @NoArgsConstructor
        @Data
        public static class FormattedAddressesBean {

            private String recommend;
            private String rough;
        }

        @NoArgsConstructor
        @Data
        public static class AddressComponentBean {

            private String nation;
            private String province;
            private String city;
            private String district;
            private String street;
            private String streetNumber;
        }

        @NoArgsConstructor
        @Data
        public static class AdInfoBean {

            private String nationCode;
            private String adcode;
            private String cityCode;
            private String name;
            private LocationBean location;
            private String nation;
            private String province;
            private String city;
            private String district;
        }
    }
}
