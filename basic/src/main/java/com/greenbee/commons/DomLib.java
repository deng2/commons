package com.greenbee.commons;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DomLib {

    public static List<Attr> getAttribute(Element ele) {
        ArrayList<Attr> list = new ArrayList<>();
        NamedNodeMap attrs = ele.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            list.add(attr);
        }
        return list;
    }

    public static List<Attr> getNamespaceAttribute(Element ele) {
        ArrayList<Attr> list = new ArrayList<>();
        for (Attr attr : getAttribute(ele)) {
            if (attr.getName().startsWith("xmlns:"))
                list.add(attr);
        }
        return list;
    }

    public static List<Element> getChildElement(Element ele) {
        ArrayList<Element> list = new ArrayList<>();
        NodeList children = ele.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                list.add((Element) child);
            }
        }
        return list;
    }

    public static String getText(Element ele) {
        NodeList children = ele.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Text) {
                String value = child.getNodeValue();
                if (!StringUtils.isEmpty(value))
                    return value;
            }

        }
        return null;
    }

    public static void addNamespace(Element ele, List<Attr> attrs) {
        for (Attr attr : attrs) {
            ele.setAttribute(attr.getName(), attr.getValue());
        }
    }

    public static void removeNamespace(Element ele, List<Attr> attrs) {
        for (Attr attr : attrs) {
            ele.removeAttribute(attr.getName());
        }
    }

}
