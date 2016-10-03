package com.time.show.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model representing the configuration result returned by API
 *
 * @author aleksandrTmk
 */
public class Configuration {
    private Image images;
    @SerializedName("change_keys")
    private String[] changeKeys;

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public String[] getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(String[] change_keys) {
        this.changeKeys = change_keys;
    }

    /**
     * Inner class to model the definition of the configuration for an image
     */
    public class Image {
        @SerializedName("base_url")
        private String baseUrl;
        @SerializedName("secure_base_url")
        private String secureBaseUrl;

        @SerializedName("backdrop_sizes")
        private String[] backdropSizes;
        @SerializedName("logo_sizes")
        private String[] logoSizes;
        @SerializedName("still_sizes")
        private String[] stillSizes;
        @SerializedName("poster_sizes")
        private String[] posterSizes;
        @SerializedName("profile_sizes")
        private String[] profileSizes;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String base_url) {
            this.baseUrl = base_url;
        }

        public String getSecureBaseUrl() {
            return secureBaseUrl;
        }

        public void setSecureBaseUrl(String secure_base_url) {
            this.secureBaseUrl = secure_base_url;
        }

        public String[] getBackdropSizes() {
            return backdropSizes;
        }

        public void setBackdropSizes(String[] backdrop_sizes) {
            this.backdropSizes = backdrop_sizes;
        }

        public String[] getLogoSizes() {
            return logoSizes;
        }

        public void setLogoSizes(String[] logo_sizes) {
            this.logoSizes = logo_sizes;
        }

        public String[] getPosterSizes() {
            return posterSizes;
        }

        public void setPosterSizes(String[] poster_sizes) {
            this.posterSizes = poster_sizes;
        }

        public String[] getProfileSizes() {
            return profileSizes;
        }

        public void setProfileSizes(String[] profile_sizes) {
            this.profileSizes = profile_sizes;
        }

        public String[] getStillSizes() {
            return stillSizes;
        }

        public void setStillSizes(String[] still_sizes) {
            this.stillSizes = still_sizes;
        }
    }

}
