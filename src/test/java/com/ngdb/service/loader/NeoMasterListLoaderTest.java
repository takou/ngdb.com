package com.ngdb.service.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ngdb.domain.Game;

@Ignore
public class NeoMasterListLoaderTest {

	@Test
	public void main() throws Exception {
		InputStream stream = NeoGeoCDWorldLoaderTest.class.getClassLoader().getResourceAsStream("sources/neogeocom.html");
		List<Game> games = new ArrayList<Game>();

		DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
		newInstance.setValidating(false);
		DocumentBuilder newDocumentBuilder = newInstance.newDocumentBuilder();
		Document document = newDocumentBuilder.parse(stream);
		NodeList childNodes = document.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			System.err.println(childNodes.item(i).getNodeName());

		}

		System.err.println(games.size());
	}
}
