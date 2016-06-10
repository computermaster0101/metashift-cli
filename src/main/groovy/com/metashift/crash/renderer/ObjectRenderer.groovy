package com.metashift.crash.renderer;

import org.crsh.text.Color;
import org.crsh.text.Decoration;
import org.crsh.text.LineRenderer;
import org.crsh.text.Renderer;
import org.crsh.text.ui.RowElement;
import org.crsh.text.ui.TableElement;

import java.util.*;

/**
 * Created by nicholaspadilla on 6/6/16.
 */
public class ObjectRenderer extends Renderer<List<?>> {

    public ObjectRenderer() { }

    public Class<List<?>> getType() {
        Class mapClass = List.class;
        return (Class)mapClass;
    }

    def Map<String, Object> toMap(object) { return object?.properties.findAll{ (it.key != 'class') }.collectEntries {
        it.value == null || it.value instanceof Serializable ? [it.key, it.value] : [it.key, toMap(it.value)]
    }
    }

    public LineRenderer renderer(Iterator<List<?>> stream) {
        TableElement table = (new TableElement()).rightCellPadding(1);
        ArrayList renderers = new ArrayList();

        boolean hasHeaders = false;
        RowElement header = new RowElement(true);
        header.style(Decoration.bold.fg(Color.black).bg(Color.white));

        while(stream.hasNext()){
            List row = (List)stream.next();
            if(!row.isEmpty()){
                for(int i = 0; i < row.size(); i++){
                    Map<String, Object> data = toMap(row.get(i))
                    RowElement dataRow = new RowElement(false);
                    for(Map.Entry entry : data.entrySet()) {
                        if(!hasHeaders) {
                            header.add(String.valueOf(entry.getKey()))
                        }
                        dataRow.add(entry.getValue().toString())
                    }

                    if(!hasHeaders && header.getSize() > 0){
                        table.add(header);
                        hasHeaders = true;
                    }

                    if(dataRow.getSize() > 0){
                        table.add(dataRow);
                    }
                }
            }
        }

        if(table.getRows().size() > 0) {
            renderers.add(table.renderer());
        }

        return LineRenderer.vertical(renderers);
    }
}
