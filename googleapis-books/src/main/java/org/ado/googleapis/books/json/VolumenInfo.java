package org.ado.googleapis.books.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description here.
 *
 * @author andoni
 * @since 25.10.2014
 */
public class VolumenInfo {
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifier> industryIdentifiers;
    private int pageCount;
    private String printType;
    private List<String> categories;
    private String averageRating;
    private int ratingsCount;
    private String contentVersion;
    private ImageLinks imageLinks;
    private String language;
    private String previewLink;
    private String infoLink;
    private String canonicalVolumeLink;

    public VolumenInfo() {
        industryIdentifiers = new ArrayList<IndustryIdentifier>();
        categories = new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IndustryIdentifier> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(List<IndustryIdentifier> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getContentVersion() {
        return contentVersion;
    }

    public void setContentVersion(String contentVersion) {
        this.contentVersion = contentVersion;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getCanonicalVolumeLink() {
        return canonicalVolumeLink;
    }

    public void setCanonicalVolumeLink(String canonicalVolumeLink) {
        this.canonicalVolumeLink = canonicalVolumeLink;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("org.ado.googleapis.books.json.VolumenInfo{");
        sb.append("title='").append(title).append('\'');
        sb.append(", authors=").append(authors);
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append(", publishedDate='").append(publishedDate).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", industryIdentifiers=").append(industryIdentifiers);
        sb.append(", pageCount=").append(pageCount);
        sb.append(", printType='").append(printType).append('\'');
        sb.append(", categories=").append(categories);
        sb.append(", averageRating='").append(averageRating).append('\'');
        sb.append(", ratingsCount=").append(ratingsCount);
        sb.append(", contentVersion='").append(contentVersion).append('\'');
        sb.append(", imageLinks=").append(imageLinks);
        sb.append(", language='").append(language).append('\'');
        sb.append(", previewLink='").append(previewLink).append('\'');
        sb.append(", infoLink='").append(infoLink).append('\'');
        sb.append(", canonicalVolumeLink='").append(canonicalVolumeLink).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
