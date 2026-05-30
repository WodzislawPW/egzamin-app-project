package pl.edu.pw.ee.egzamin_app_project;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocxParser
{
    Test test;
    File directory;
    int groupAmount = 1;
    boolean random = true;

    public void setTest(Test test)
    {
        this.test = test;
    }

    public void setDirectory(File directory) { this.directory = directory; }

    public void setGroupAmount(int groupAmount) { this.groupAmount = groupAmount; }

    public void setRandom(boolean random) { this.random = random; }

    public void exportTest()
    {
        String letters = "ABCDEFGHIJ";

        String output = directory.toString() + "\\" + test.toString() + "_AnswerSheet" + ".docx";
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph answers = document.createParagraph();
        answers.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun answersRun = answers.createRun();

        for(int i=0; i<groupAmount; i++)
        {
            answersRun.setText("Grupa " + letters.charAt(i));
            answersRun.addBreak();
            toDocx(letters.charAt(i), answersRun);
            answersRun.addBreak();
        }

        try
        {
            FileOutputStream out = new FileOutputStream(output);
            document.write(out);
            out.close();
            document.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void toDocx(char group, XWPFRun answersRun)
    {
        List<Question> questions = new ArrayList<>(test.getQuestions());
        String letters = "ABCD";

        if(random)
            Collections.shuffle(questions);

        String output = directory.toString() + "\\" + test.toString() + "_" + group + ".docx";
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph tasks = document.createParagraph();
        tasks.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tasksRun = tasks.createRun();

        int i = 1;
        for(Question q : questions)
        {
            if(!q.getOpen())
                q.randomizeAnswerOrder();

            tasksRun.setText(i + ". ");
            q.docxString(tasksRun);

            tasksRun.addBreak();

            if(!q.getOpen())
            {
                //answerSheet.append(i);
                //answerSheet.append(letters.charAt(q.giveIndexOfCorrectAnswer()));
                //answerSheet.append("\n");

                answersRun.setText(i + "");
                answersRun.setText(letters.charAt(q.giveIndexOfCorrectAnswer()) + "");
                answersRun.addBreak();
            }
            i++;
        }

        try
        {
            FileOutputStream out = new FileOutputStream(output);
            document.write(out);
            out.close();
            document.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println(output);
    }
}
