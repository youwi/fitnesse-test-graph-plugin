package fitnesse.plugin.graph;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import fitnesse.wiki.WikiPageUtil;
import fitnesse.wiki.fs.FileSystemPage;
import fitnesse.wiki.fs.WikiFilePage;
import fitnesse.wikitext.parser.CompositeVariableSource;
import fitnesse.wikitext.parser.Maybe;
import fitnesse.wikitext.parser.ParsingPage;
import fitnesse.wikitext.parser.VariableSource;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

/**
 * fitnesse @sec.com
 * Created by yu on 2017/7/20.
 */
public class PageVarsResponder implements Responder {
    /**
     * simplie clone
     *
     * @param context
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {

        WikiPage rootPage = context.getRootPage(request.getMap());

        SimpleResponse response = new SimpleResponse();
        response.setContentType("application/json;charset=utf-8");
        try {
            String resource = request.getResource();
            response.setContent(buildVarTagsJson(context, resource));

        } catch (Exception e) {
            e.printStackTrace();
            response.setContent("{error:1,msg:'" + e.getMessage() + "'}");
        }

        return response;
    }

    public static String buildVarTagsJson(FitNesseContext context, String resource) {
        return new JSONObject(getVarTagsMap(context, resource)).toString();
    }

    public static String buildVarTagsWikiText(FitNesseContext context, String resource) {
        StringBuilder out=new StringBuilder();
        Map<String, Object> src = getVarTagsMap(context, resource);
        for (String key : src.keySet()) {
            out.append("!define  ");
            out.append(key);
            out.append(" (");

            Object tar=src.get(key);
            if(tar instanceof Maybe){
                out.append(((Maybe)tar).getValue());
            }

            out.append(")\n");
        }
        return out.toString();
    }

    public static Map getVarTagsMap(FitNesseContext context, String resource) {
        WikiPagePath path = PathParser.parse(resource);
        WikiPage page = context.getRootPage().getPageCrawler().getPage(path);
        Map<String, Object> out = new HashMap();
        List<Map> hisCache = new ArrayList();
        List<Map> overLoads = new ArrayList();

        //String vars = page.getVariable("A");
        while (page != null) {
            if (page.isRoot()) {
                break;
            }
            ParsingPage pp = null;
            if (page instanceof FileSystemPage) {
                pp = ((FileSystemPage) page).getParsingPage();
            }
            if (page instanceof WikiFilePage) {
                pp = ((WikiFilePage) page).getParsingPage();
            }
            if (pp != null) {
                ParsingPage.Cache cacheObj = (ParsingPage.Cache) getField(pp, "cache");
                Map map = (Map) getField(cacheObj, "cache");
                hisCache.add(map);
            }
            page = page.getParent();
        }
        //后入先出
        Collections.reverse(hisCache);
        for (Map map : hisCache) {
            for (String key : out.keySet()) {
                if (map.get(key) != null) {
                    Map temp = new HashMap();
                    temp.put(key, out.get(key));
                    overLoads.add(temp);
                }
                out.get(key).equals(map.get(key));
            }
            out.putAll(map);
        }
        out.put("__OVER_LOADS__", overLoads);
        return out;
    }


    public static Object getField(Object src, String name) {
        Field field = null; //NoSuchFieldException
        try {
            field = src.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(src);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
