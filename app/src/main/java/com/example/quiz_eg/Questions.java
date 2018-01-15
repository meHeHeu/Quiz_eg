package com.example.quiz_eg;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

class Questions {

    private Question[] question;

    class Question {

        public final String body;
        public final String answer;
        public final String[] badAnswer;

        Question (String body, String answer, String... badAnswer) {
            this.body = body;
            this.answer = answer;
            this.badAnswer = badAnswer;
        }
    }
    /*

    :O
    https://developer.android.com/training/basics/network-ops/xml.html
    Zrobiłem to WŁAŚNIE W TEN SPOSÓB zanim jeszcze zacząłem opracowywać parser.
    (wewnętrzna klasa, pola publiczne, finalne; konstruktor)
    Jestem zajebisty.

     */

    class TestXmlParser {

        private final String ns = null;

        public List<Question> parse(FileInputStream fileInputStreams)
                throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(fileInputStreams, null);
                parser.nextTag();
                return readTest(parser);
            } finally {
                fileInputStreams.close();
            }
        }

        private List<Question> readTest(XmlPullParser parser)
                throws XmlPullParserException, IOException {

            ArrayList<Question> result = new ArrayList<Question>();

            parser.require(XmlPullParser.START_TAG, ns, "test");

            while(parser.next() != XmlPullParser.END_TAG) {

                if(parser.getEventType() != XmlPullParser.START_TAG)
                    continue;

                String name = parser.getName();
                if(name.equals("question"))
                    result.add(readQuestion(parser));
                else
                    skip(parser);
            }

            return result;
        }

        private Question readQuestion(XmlPullParser parser)
                throws XmlPullParserException, IOException {

            String body = null;
            String answer = null;
            String[] banswer = new String[3];
            int banswer_num = 0;

            parser.require(XmlPullParser.START_TAG, ns, "question");

            while(parser.next() != XmlPullParser.END_TAG) {

                if(parser.getEventType() != XmlPullParser.START_TAG)
                    continue;

                String name = parser.getName();
                if(name.equals("banswer"))
                    banswer[banswer_num++] = readEntry(parser, "banswer"); // FIXME: if more entries of banswer found in a xml file, this will crash an app (or cause undefined behaviour)
                else if(name.equals("answer"))
                    answer = readEntry(parser, "answer");
                else if(name.equals("body"))
                    body = readEntry(parser, "body");
                else
                    skip(parser);
            }

            return new Question(body, answer, banswer);
        }

        private String readEntry(XmlPullParser parser, String entryName)
                throws XmlPullParserException, IOException {

            parser.require(XmlPullParser.START_TAG, ns, entryName);
            String result = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, entryName);

            return result;
        }

        private String readText(XmlPullParser parser)
                throws XmlPullParserException, IOException {

            String result = "";

            if(parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }

            return result;
        }

        private void skip(XmlPullParser parser)
                throws XmlPullParserException, IOException {

            if(parser.getEventType() != XmlPullParser.START_TAG)
                throw new IllegalStateException();

            int depth = 1;
            while(depth != 0) {
                switch(parser.next()) {
                    case XmlPullParser.END_TAG:
                        --depth;
                        break;
                    case XmlPullParser.START_TAG:
                        ++depth;
                        break;
                }
            }
        }

        public void ExampleParsing()
                throws XmlPullParserException, IOException {
            XmlPullParser xpp = Xml.newPullParser();

            xpp.setInput( new StringReader( "<foo>Hello World!</foo>" ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.i("ExampleParsing", "Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    Log.i("ExampleParsing", "Start tag "+xpp.getName());
                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.i("ExampleParsing", "End tag "+xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    Log.i("ExampleParsing", "Text "+xpp.getText());
                }
                eventType = xpp.next();
            }
            Log.i("ExampleParsing", "End document");
        }
    }

    Questions(String ...fileNames) {

        List<File> quizFiles = new ArrayList<File>();
        List<Question> quizQuestions = new ArrayList<Question>();
        TestXmlParser testXmlParser = new TestXmlParser();

        for(String fileName : fileNames)
            quizFiles.addAll(MyUtils.FileListByExtension(new File(fileName), new String[]{".xml"}));

        for(File file : quizFiles)
            try {
                quizQuestions.addAll(testXmlParser.parse(new FileInputStream(file)));
            } catch(IOException e) {
                e.printStackTrace();
                continue;
            } catch(XmlPullParserException e) {
                e.printStackTrace();
                continue;
            }

        question = quizQuestions.toArray(new Question[quizQuestions.size()]);

        MyUtils.<Question>ShuffleArray(question);
    }

    Question getQuestion(int i) {
        return question[i];
    }

    int amount() {
        return question.length;
    }
}
