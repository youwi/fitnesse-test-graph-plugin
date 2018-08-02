package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.editing.EditResponder;
import fitnesse.responders.editing.SaveRecorder;
import fitnesse.wiki.*;
import org.json.JSONObject;

import java.util.List;

import static fitnesse.util.ParseFit.getFitTables;
import static fitnesse.util.ParseFit.parseTextToList;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class SaveByPosResponder implements Responder {
  /**
   * 通过位置参数保存内容.
   * {
   * oldValue:String ,
   * newValue:String,
   * {
   * indexTable:int,
   * indexTr:int,
   * indexTd:int
   * }
   * }
   *
   * @param context Fitnesse Context
   * @param request HttpRequest
   * @return
   * @throws Exception
   */
  @Override
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {

    String body = request.getBody();
    JSONObject object = new JSONObject(body);
    JSONObject positionObj = object.getJSONObject("position");
    Pos position = new Pos();
    position.indexTable = positionObj.getInt("indexTable");
    position.indexTr = positionObj.getInt("indexTr");
    position.indexTd = positionObj.getInt("indexTd");


    SimpleResponse response = new SimpleResponse();
    response.setContentType("application/json;charset=utf-8");
    String resource = request.getResource();

    WikiPagePath path = PathParser.parse(resource);
    WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
    PageData pageData = page.getData();
    String content = pageData.getContent();
    content = formatContentByPos(content, object.getString("oldValue"), object.getString("newValue"), position);
    pageData.setContent(content);
    page.commit(pageData);
    SaveRecorder.pageSaved(page, getTicketId(request));
    VersionInfo commitRecord = page.commit(pageData);

    try {
      response.setContent("{}");
    } catch (Exception e) {
      e.printStackTrace();
      response.setContent("{error:1,msg:'" + e.getMessage() + "'}");
    }
    return response;
  }

  public static String formatContentByPos(String content, String oldValue, String newValue, Pos pos) {
    List allTables = parseTextToList(content);
    List tables = getFitTables(allTables);
    Object rows = tables.get(pos.indexTable);
    if (rows instanceof List) {
      String trLine = ((List) rows).get(pos.indexTr).toString();
      String[] cells = trLine.split("\\|");
      String oriValue = cells[pos.indexTd + 1];
      if (oldValue.trim().equals(oriValue.trim())) {
        cells[pos.indexTd + 1] = newValue;
        String back=String.join("|", cells);
        //patch for ...
        if(trLine.endsWith("|") && !back.endsWith("|")){
          back+="|";
        }
        ((List) rows).set(pos.indexTr, back);
      }
    }
    String out = "";
    for (Object inList : allTables) {
      out += (String.join("\n", (List) inList) + "\n");
    }
    return out;
  }

  private long getTicketId(Request request) {
    if (!request.hasInput(EditResponder.TICKET_ID))
      return 0;
    String ticketIdString = request.getInput(EditResponder.TICKET_ID);
    return Long.parseLong(ticketIdString);
  }

  public static class Pos {
    public Pos() {
    }

    public int indexTable;
    public int indexTr;
    public int indexTd;

    public Pos(int indexTable, int indexTr, int indexTd) {
      this.indexTable = indexTable;
      this.indexTr = indexTr;
      this.indexTd = indexTd;
    }
  }


}
