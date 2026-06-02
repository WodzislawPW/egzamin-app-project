package pl.edu.pw.ee.egzamin_app_project;

import javafx.scene.control.Alert;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocxParser
{
    Test test;
    File directory;
    int groupAmount = 1;
    boolean random = true;
    boolean print = true;

    public void setTest(Test test)
    {
        this.test = test;
    }

    public void setDirectory(File directory) { this.directory = directory; }

    public void setGroupAmount(int groupAmount) { this.groupAmount = groupAmount; }

    public void setRandom(boolean random) { this.random = random; }

    public void setPrint(boolean print) { this.print = print; }

    public void exportTest()
    {
        String groupLetters = "ABCDEFGHIJ";

        String output = directory.toString() + "\\" + test.toString() + "_AnswerSheet" + ".docx";
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph answers = document.createParagraph();
        answers.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun answersRun = answers.createRun();

        int tempGroupAmount = groupAmount;

        if(!print)
            tempGroupAmount = 1;

        for(int i=0; i<tempGroupAmount; i++)
        {
            answersRun.setText("Grupa " + groupLetters.charAt(i));
            answersRun.addBreak();
            toDocx(groupLetters.charAt(i), answersRun);
            answersRun.addBreak();
        }


        if(print)
        {
            try {
                FileOutputStream out = new FileOutputStream(output);
                document.write(out);
                out.close();
                document.close();
            } catch (IOException e)
            {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Eksport");
                alert.setHeaderText(null);
                alert.setContentText("Eksport karty odpowiedzi " + test.getName() + " się nie udał.");
                alert.showAndWait();
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Eksport");
        alert.setHeaderText(null);
        alert.setContentText("Eksport testu " + test.getName() + " się udał.");
        alert.showAndWait();

        tempGroupAmount = groupAmount;
    }

    public void toDocx(char group, XWPFRun answersRun)
    {
        List<Question> questions = new ArrayList<>(test.getQuestions());
        String answerLetters = "ABCD";

        if(random && print)
            Collections.shuffle(questions);

        String output;
        String docxOutput;
        String pdfOutput;
        if(print)
            output = directory.toString() + "\\" + test.toString() + "_" + group;
        else
            output = directory.toString() + "\\" + test.toString();

        docxOutput = output + ".docx";
        pdfOutput = output + ".pdf";

        XWPFDocument document = new XWPFDocument();

        if(print)
        {
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();

            titleRun.setText(test.getName() + " - Grupa " + group);
            titleRun.setBold(true);
            titleRun.setFontSize(20);


            XWPFParagraph data = document.createParagraph();
            data.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun dataRun = data.createRun();

            dataRun.setText("Imię i nazwisko: ");
            dataRun.addBreak();
            dataRun.setText("Nr. Albumu: ");
            dataRun.addBreak();
            dataRun.setText("Grupa dz.: ");
            dataRun.addBreak();
        }


        XWPFParagraph tasks = document.createParagraph();
        tasks.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tasksRun = tasks.createRun();

        int i = 1;
        for(Question q : questions)
        {
            if(!q.getOpen() && print)
                q.randomizeAnswerOrder();

            tasksRun.setText(i + ". ");
            q.docxString(tasksRun);

            tasksRun.addBreak();

            if(!q.getOpen())
            {
                answersRun.setText(i + "");
                answersRun.setText(answerLetters.charAt(q.giveIndexOfCorrectAnswer()) + "");
                answersRun.addBreak();
            }
            else
            {
                tasksRun.addBreak();
                tasksRun.addBreak();
                tasksRun.addBreak();
            }
            i++;
        }

        try
        {
            FileOutputStream out = new FileOutputStream(docxOutput);
            document.write(out);
            out.close();
            document.close();

            if(print)
            {
                WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(docxOutput));

                try (OutputStream os = new FileOutputStream(pdfOutput))
                {
                    Docx4J.toPDF(wordMLPackage, os);
                }
            }


        }
        catch (IOException | Docx4JException e)
        {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eksport");
            alert.setHeaderText(null);
            alert.setContentText("Eksport testu " + test.getName() + " się nie udał.");
            alert.showAndWait();

            return;
        }
    }
}
