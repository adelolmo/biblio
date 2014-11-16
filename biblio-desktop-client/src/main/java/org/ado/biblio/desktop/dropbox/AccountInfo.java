package org.ado.biblio.desktop.dropbox;

/**
 * Class description here.
 *
 * @author andoni
 * @since 16.11.2014
 */
public class AccountInfo {
    public long userId;
    public String displayName;
    public String country;
    public String referralLink;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReferralLink() {
        return referralLink;
    }

    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountInfo that = (AccountInfo) o;

        if (userId != that.userId) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (referralLink != null ? !referralLink.equals(that.referralLink) : that.referralLink != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (referralLink != null ? referralLink.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountInfo{");
        sb.append("userId=").append(userId);
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", referralLink='").append(referralLink).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
