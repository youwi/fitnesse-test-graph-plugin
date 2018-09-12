package fitnesse.plugin.graph;

import fitnesse.components.TraversalListener;
import fitnesse.wiki.*;
import fitnesse.wikitext.parser.VariableSource;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class VirtualWikiPage implements WikiPage {

  @Override
  public WikiPage getParent() {
    return null;
  }

  @Override
  public boolean isRoot() {
    return false;
  }

  @Override
  public WikiPage addChildPage(String name) {
    return null;
  }

  @Override
  public boolean hasChildPage(String name) {
    return false;
  }

  @Override
  public WikiPage getChildPage(String name) {
    return null;
  }

  @Override
  public void removeChildPage(String name) {

  }

  @Override
  public void remove() {

  }

  @Override
  public List<WikiPage> getChildren() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public PageData getData() {
    return new PageData("Handle By Proxy. this Page can to be edit.", new WikiPageProperty());
  }

  @Override
  public Collection<VersionInfo> getVersions() {
    return null;
  }

  @Override
  public WikiPage getVersion(String versionName) {
    return null;
  }

  @Override
  public String getHtml() {
    return "Handle By Proxy. this Page can to be edit.";
  }

  @Override
  public VersionInfo commit(PageData data) {
    return null;
  }

  @Override
  public PageCrawler getPageCrawler() {
    return new PageCrawler( null) {
      @Override
      public WikiPage getPage(WikiPagePath path) {
        return null;
      }

      @Override
      public WikiPage getPage(WikiPagePath path, PageCrawlerDeadEndStrategy deadEndStrategy) {
        return null;
      }

      @Override
      public boolean pageExists(WikiPagePath path) {
        return false;
      }

      @Override
      public WikiPagePath getFullPathOfChild(WikiPagePath childPath) {
        return null;
      }

      @Override
      public WikiPagePath getFullPath() {
        return new WikiPagePath();
      }

      @Override
      public String getRelativeName(WikiPage page) {
        return null;
      }

      @Override
      public WikiPage getRoot() {
        return null;
      }

      @Override
      public void traversePageAndAncestors(TraversalListener<? super WikiPage> callback) {

      }

      @Override
      public void traverseUncles(String uncleName, TraversalListener<? super WikiPage> callback) {

      }

      @Override
      public WikiPage getSiblingPage(WikiPagePath pathRelativeToSibling) {
        return null;
      }

      @Override
      public WikiPage findAncestorWithName(String name) {
        return null;
      }

      @Override
      public WikiPage getClosestInheritedPage(String pageName) {
        return null;
      }
    };
  }

  @Override
  public String getVariable(String name) {
    return null;
  }

  @Override
  public int compareTo(WikiPage o) {
    return 0;
  }
}
