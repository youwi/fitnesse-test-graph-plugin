package fitnesse.plugin.graph;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PageType;
import fitnesse.wiki.WikiImportProperty;
import fitnesse.wikitext.parser.*;

import java.util.List;

import static fitnesse.util.BeanUtil.getObjectByPath;

/**
 * pluginTestGraph
 * Created by yu on 2018/7/27.
 */
public class PageContentsNormalNameSymbolType extends SymbolType implements Rule, Translation {

    public PageContentsNormalNameSymbolType() {
        super("ContentsNormalName");
        wikiMatcher(new Matcher().startLineOrCell().string("!contentx"));
        wikiRule(this);
        htmlTranslation(this);
    }

    @Override
    public Maybe<Symbol> parse(Symbol current, Parser parser) {
        Symbol body = parser.parseToEnd(SymbolType.Newline);
        for (Symbol option : body.getChildren()) {
            if (option.isType(SymbolType.Whitespace)) continue;
            if (!option.getContent().startsWith("-")) return Symbol.nothing;
            current.add(option);
        }

        return new Maybe<>(current);
    }

    @Override
    public String toTarget(Translator translator, Symbol symbol) {
        ContentsItemBuilder itemBuilder = new PageCinContentsItemBuilder(symbol, 1, translator.getPage());
        HtmlTag contentsDiv = new HtmlTag("div");
        contentsDiv.addAttribute("class", "contents");
        contentsDiv.add(HtmlUtil.makeBold("Contents:"));
        contentsDiv.add(itemBuilder.buildLevel(translator.getPage()));
        // ((HtmlTag) ((HtmlTag) contentsDiv.childTags.last.item).childTags.last.item).childTags
        try {
            List<HtmlTag> list = (List) getObjectByPath(contentsDiv, "childTags");
            HtmlTag tag = list.get(list.size() - 1);
            list = (List) getObjectByPath(tag, "childTags");
            for (HtmlTag htmlTag : list) {
                System.out.println(htmlTag);
                //htmlTag.

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentsDiv.html();
    }


}
