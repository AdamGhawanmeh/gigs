package com.schoolproject.gigs;

public class flashcardWid {
  private String frontText,backText;
  private boolean showingBack;
  private String ID;
  private Long study;

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public Long getStudy() {
    return study;
  }

  public void setStudy(Long study) {
    this.study = study;
  }

  public boolean isShowingBack() {
    return showingBack;
  }

  public void setShowingBack(boolean showingBack) {
    this.showingBack = showingBack;
  }

  public flashcardWid(String frontText, String backText) {
    this.frontText = frontText;
    this.backText = backText;
    study = Long.valueOf(0);
  }

  public String getFrontText() {
    return frontText;
  }

  public void setFrontText(String frontText) {
    this.frontText = frontText;
  }

  public String getBackText() {
    return backText;
  }

  public void setBackText(String backText) {
    this.backText = backText;
  }

  public flashcardWid() {
  }
}
