package org.ado.biblio.desktop.dropbox;

import com.dropbox.core.DbxAccountInfo;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class AccountInfoFactory {

    public static AccountInfo getAccountInfo(DbxAccountInfo dropboxAccountInfo) {
        final AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCountry(dropboxAccountInfo.country);
        accountInfo.setDisplayName(dropboxAccountInfo.displayName);
        accountInfo.setReferralLink(dropboxAccountInfo.referralLink);
        accountInfo.setUserId(dropboxAccountInfo.userId);
        return accountInfo;
    }
}
