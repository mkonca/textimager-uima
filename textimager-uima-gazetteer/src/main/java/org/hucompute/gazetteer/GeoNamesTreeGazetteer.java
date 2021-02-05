package org.hucompute.gazetteer;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.jcas.JCas;
import org.texttechnologylab.annotation.GeoNamesEntity;

import java.net.URI;
import java.util.stream.Collectors;

public class GeoNamesTreeGazetteer extends GeneralTreeGazetteer {


    @Override
    protected void addElement(JCas aJCas, Match match) {
        try {
            Token fromToken = tokens.get(tokenBeginIndex.get(match.start));
            Token toToken = tokens.get(tokenEndIndex.get(match.end));
            GeoNamesEntity geoNames = new GeoNamesEntity(aJCas, fromToken.getBegin(), toToken.getEnd());

            String el = skipGramGazetteerModel.skipGramElementLookup.get(match.value);
            String sID = skipGramGazetteerModel.elementUriMap.get(el).stream()
                    .map(URI::toString)
                    .collect(Collectors.joining(","));
            geoNames.setId(Integer.valueOf(sID));
            aJCas.addFsToIndexes(geoNames);
        } catch (NullPointerException e) {
            // FIXME: Remove this
            System.err.println(e.getMessage());
            System.err.println(aJCas.getDocumentText().substring(match.start, match.end + 10));
            System.err.println(match.value);
            e.printStackTrace();
        }
    }
}
