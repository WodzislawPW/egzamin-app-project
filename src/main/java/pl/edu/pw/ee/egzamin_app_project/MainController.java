package pl.edu.pw.ee.egzamin_app_project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    private Label correctAnswerLabel, incorrectAnswerLabel1, incorrectAnswerLabel2, incorrectAnswerLabel3, missingElementsLabel, categoryStatusLabel, testStatusLabel;

    @FXML
    private TextArea questionTextArea, answersTextArea;

    @FXML
    private TextField correctAnswerTextField, incorrectAnswerTextField1, incorrectAnswerTextField2,incorrectAnswerTextField3, categoryTextField, searchTextField, testNameTextField, testSearchTextField, questionSearchTextField;

    @FXML
    private Button confirmButton, clearButton, addCategoryButton, deleteCategoryButton, deleteQuestionButton, addTestButton, deleteTestButton, deleteTestButton2, addQuestionToTestButton, removeQuestionFromTestButton;

    @FXML
    private ChoiceBox<String> questionAmountChoiceBox, categoryChoiceBox;

    @FXML
    private ListView<Question> questionListView, testQuestionListView, searchedQuestionListView;

    @FXML
    private ListView<Test> testListView;

    private String[] incorrectAnswerAmount = {"Otwarte", "1", "2", "3"};
    private String questionType = incorrectAnswerAmount[3];
    private int questionAmount = 3;
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), e -> fadeOutMissingLabel()));

    QuestionService questionService;
    CategoryService categoryService;
    TestService testService;

    private List<String> categories;
    private boolean categoryExists = true;
    private boolean categoryLabelEmpty = true;
    private boolean testExists = true;
    private boolean testLabelEmpty = true;
    private boolean searchQuestion = true;

    Question questionForDeletion, currentQuestion;
    Test testForDeletion, currentTest = new Test("temp");


    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        questionAmountChoiceBox.getItems().addAll(incorrectAnswerAmount);
        questionAmountChoiceBox.getSelectionModel().select(3);
        questionAmountChoiceBox.setOnAction(this::switchAnswerAmount);

        categoryService = new CategoryService();

        inputCategories();

        questionService = new QuestionService();
        setupQuestionListView();

        updateCategoryButtons();

        answersTextArea.setEditable(false);
        answersTextArea.setVisible(false);

        testService = new TestService();

        setupTestListView();
        setupSearchedQuestionListView();

        updateTestButtons();

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();

        deleteQuestionButton.setDisable(true);
        deleteTestButton2.setDisable(true);
        addQuestionToTestButton.setDisable(true);
        removeQuestionFromTestButton.setDisable(true);

        //System.out.println(categoryChoiceBox.getSelectionModel().getSelectedItem());
    }

    public void deleteQuestion(ActionEvent event)
    {
        questionService.deleteQuestion(questionForDeletion);
        deleteQuestionButton.setDisable(true);
        updateQuestionListView();
    }

    public void updateQuestionListViewKeyword(KeyEvent event)
    {
        String text = searchTextField.getText();
        questionService.setKeyword(text);
        updateQuestionListView();
    }

    public void updateCategoryStatusLabelBuffer(KeyEvent event)
    {
        updateCategoryStatusLabel();
    }

    private void updateCategoryStatusLabel()
    {
        String text = categoryTextField.getText();

        if(categories.contains(text))
        {
            categoryStatusLabel.setText("Taka kategoria już istnieje");
            categoryExists = true;
        }
        else
        {
            categoryStatusLabel.setText("Nowa kategoria");
            categoryExists = false;
        }

        if(text.isEmpty())
        {
            categoryStatusLabel.setText("");
            categoryExists = true;
            categoryLabelEmpty = true;
        }
        else
            categoryLabelEmpty = false;

        updateCategoryButtons();
    }

    private void updateCategoryButtons()
    {
        if(categoryExists)
        {
            addCategoryButton.setDisable(true);
            deleteCategoryButton.setDisable(false);
        }
        else
        {
            addCategoryButton.setDisable(false);
            deleteCategoryButton.setDisable(true);
        }

        if(categoryLabelEmpty)
        {
            addCategoryButton.setDisable(true);
            deleteCategoryButton.setDisable(true);
        }
    }

    private void inputCategories()
    {
        categories = categoryService.getCategories();
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().addAll(categories);
    }

    public void addNewCategory(ActionEvent event)
    {
        String newCategory = categoryTextField.getText();

        categoryService.saveCategory(newCategory);
        inputCategories();

        updateCategoryStatusLabel();
    }

    public void removeCategory(ActionEvent event)
    {
        String removedCategory = categoryTextField.getText();

        categoryService.removeCategory(removedCategory);
        inputCategories();

        updateCategoryStatusLabel();
    }

    public void updateTestListViewKeyword(KeyEvent event)
    {
        String text = testSearchTextField.getText();
        testService.setKeyword(text);
        updateTestListView();
    }

    public void updateTestStatusLabelBuffer(KeyEvent event)
    {
        updateTestStatusLabel();
    }

    private void updateTestStatusLabel()
    {
        String text = testNameTextField.getText();

        if(testService.getTest(text)!=null)
        {
            testStatusLabel.setText("Taki test już istnieje");
            testExists = true;
        }
        else
        {
            testStatusLabel.setText("Nowy test");
            testExists = false;
        }

        if(text.isEmpty())
        {
            testStatusLabel.setText("");
            testExists = true;
            testLabelEmpty = true;
        }
        else
            testLabelEmpty = false;

        updateTestButtons();
    }

    private void updateTestButtons()
    {
        if(testExists)
        {
            addTestButton.setDisable(true);
            deleteTestButton.setDisable(false);
        }
        else
        {
            addTestButton.setDisable(false);
            deleteTestButton.setDisable(true);
        }

        if(testLabelEmpty)
        {
            addTestButton.setDisable(true);
            deleteTestButton.setDisable(true);
        }
    }

    public void addNewTest(ActionEvent event)
    {
        Test newTest = new Test(testNameTextField.getText());

        testService.saveTests(newTest);

        updateTestListView();
        updateTestStatusLabel();
    }

    public void removeTest(ActionEvent event)
    {
        Test removedTest = testService.getTest(testNameTextField.getText());

        testService.removeTest(removedTest);

        updateTestListView();
        updateSearchedQuestionListView();
        updateTestQuestionListView();
        updateTestStatusLabel();
    }

    public void removeTest2(ActionEvent event)
    {
        testService.removeTest(testForDeletion);

        currentTest = new Test("temp");
        updateTestListView();
        updateTestStatusLabel();

        updateSearchedQuestionListView();
        updateTestQuestionListView();
    }

    public void addQuestionToTest(ActionEvent event)
    {
        //System.out.println("test");
        currentTest.addQuestion(currentQuestion);
        testService.updateTest(currentTest);

        updateTestQuestionListView();
        updateSearchedQuestionListView();
    }

    public void removeQuestionFromTest(ActionEvent event)
    {
        currentTest.removeQuestion(currentQuestion);
        testService.updateTest(currentTest);

        updateTestQuestionListView();
        updateSearchedQuestionListView();
    }

    private void setupQuestionListView()
    {
        questionListView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(Question q, boolean empty)
            {
                super.updateItem(q, empty);

                if (empty || q == null)
                {
                    setText(null);
                } else
                {
                    setText(q.toString());
                }
            }
        });

        updateQuestionListView();

        questionListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    questionForDeletion = newVal;
                    deleteQuestionButton.setDisable(false);

                    if (newVal != null && newVal.answers()!=null)
                    {
                        answersTextArea.setVisible(true);
                        answersTextArea.setText(newVal.answers());
                        //System.out.println("Wybrane: " + newVal.answers());
                    }
                    else
                        answersTextArea.setVisible(false);
                }
        );
    }

    private void updateQuestionListView()
    {
        questionListView.setItems(FXCollections.observableArrayList(questionService.getQuestions()));
    }

    private void setupTestListView()
    {
        testListView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(Test t, boolean empty)
            {
                super.updateItem(t, empty);

                if (empty || t == null)
                {
                    setText(null);
                } else
                {
                    setText(t.toString());
                }
            }
        });

        updateTestListView();

        testListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if(newVal!=null)
                    {
                        testForDeletion = newVal;
                        deleteTestButton2.setDisable(false);
                        currentTest = newVal;
                        setupSearchedQuestionListView();
                        setupTestQuestionListView();
                    }
                    else
                        deleteTestButton2.setDisable(true);
                }
        );
    }

    private void updateTestListView()
    {
        testListView.setItems(FXCollections.observableArrayList(testService.getTests()));
    }

    private void setupSearchedQuestionListView()
    {
        searchedQuestionListView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(Question q, boolean empty)
            {
                super.updateItem(q, empty);

                if (empty || q == null)
                {
                    setText(null);
                } else
                {
                    setText(q.toString());
                }
            }
        });

        updateSearchedQuestionListView();

        searchedQuestionListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    currentQuestion = newVal;
                    addQuestionToTestButton.setDisable(false);
                    removeQuestionFromTestButton.setDisable(true);
                }
        );
    }

    private void updateSearchedQuestionListView()
    {
        List<Question> searchedQuestions = questionService.getQuestions2();
        searchedQuestions.removeAll(currentTest.getQuestions());

        searchedQuestionListView.setItems(FXCollections.observableArrayList(searchedQuestions));
    }

    private void setupTestQuestionListView()
    {
        testQuestionListView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(Question q, boolean empty)
            {
                super.updateItem(q, empty);

                if (empty || q == null)
                {
                    setText(null);
                } else
                {
                    setText(q.toString());
                }
            }
        });

        updateTestQuestionListView();

        testQuestionListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    currentQuestion = newVal;
                    addQuestionToTestButton.setDisable(true);
                    removeQuestionFromTestButton.setDisable(false);
                }
        );
    }

    private void updateTestQuestionListView()
    {
        List<Question> testQuestions = currentTest.getQuestions();

        testQuestionListView.setItems(FXCollections.observableArrayList(testQuestions));
    }




    public void confirmQuestion(ActionEvent event)
    {
        List<String> missingComponents = new ArrayList<>();
        List<String> presentComponents = new ArrayList<>();

        if(categoryChoiceBox.getSelectionModel().getSelectedItem()==null)
            missingComponents.add("Kategoria");
        else
            presentComponents.add(categoryChoiceBox.getSelectionModel().getSelectedItem());


        if(questionTextArea.getText().isEmpty())
            missingComponents.add("Treść pytania");
        else
            presentComponents.add(questionTextArea.getText());


        if(questionAmount!=0)
        {
            if(correctAnswerTextField.getText().isEmpty())
                missingComponents.add("Poprawna odpowiedź");
            else
                presentComponents.add(correctAnswerTextField.getText());

            if(incorrectAnswerTextField1.getText().isEmpty())
                missingComponents.add("Niepoprawna odpowiedź 1");
            else
                presentComponents.add(incorrectAnswerTextField1.getText());

            if(questionAmount>1)
            {
                if(incorrectAnswerTextField2.getText().isEmpty())
                    missingComponents.add("Niepoprawna odpowiedź 2");
                else
                    presentComponents.add(incorrectAnswerTextField2.getText());

                if(questionAmount>2)
                {
                    if(incorrectAnswerTextField3.getText().isEmpty())
                        missingComponents.add("Niepoprawna odpowiedź 3");
                    else
                        presentComponents.add(incorrectAnswerTextField3.getText());
                }
            }
        }

        if(missingComponents.isEmpty())
        {
            Question question = new Question(questionAmount, presentComponents);
            question.printString();
            questionService.saveQuestion(question);
            questionListView.setItems(FXCollections.observableArrayList(questionService.getQuestions()));
        }
        else
        {
            int n = missingComponents.size();
            StringBuilder out = new StringBuilder("Brakuje: ");

            for(int i=0; i<n; i++)
            {
                out.append(missingComponents.get(i));
                if(i!=n-1)
                    out.append(", ");
            }

            missingElementsLabel.setText(out.toString());
            missingElementsLabel.setOpacity(1);
            timeline.play();
        }
    }

    private void fadeOutMissingLabel()
    {
        missingElementsLabel.setOpacity(missingElementsLabel.getOpacity()-0.002);

        if(missingElementsLabel.getOpacity()<0)
            timeline.pause();
    }

    public void clearFields(ActionEvent event)
    {
        questionTextArea.clear();
        correctAnswerTextField.clear();
        incorrectAnswerTextField1.clear();
        incorrectAnswerTextField2.clear();
        incorrectAnswerTextField3.clear();
        categoryChoiceBox.getSelectionModel().clearSelection();
    }

    public void switchAnswerAmount(ActionEvent event)
    {
        int amount = questionAmountChoiceBox.getSelectionModel().getSelectedIndex();
        questionType = incorrectAnswerAmount[amount];
        questionAmount = amount;

        if(amount==0)
        {
            correctAnswerLabel.setVisible(false);
            correctAnswerTextField.setVisible(false);
            correctAnswerTextField.clear();

            incorrectAnswerLabel1.setVisible(false);
            incorrectAnswerTextField1.setVisible(false);
            incorrectAnswerTextField1.clear();
            incorrectAnswerLabel2.setVisible(false);
            incorrectAnswerTextField2.setVisible(false);
            incorrectAnswerTextField2.clear();
            incorrectAnswerLabel3.setVisible(false);
            incorrectAnswerTextField3.setVisible(false);
            incorrectAnswerTextField3.clear();
        }
        else
        {
            if(amount<=3)
            {
                correctAnswerLabel.setVisible(true);
                correctAnswerTextField.setVisible(true);

                incorrectAnswerLabel1.setVisible(true);
                incorrectAnswerTextField1.setVisible(true);

                incorrectAnswerLabel2.setVisible(true);
                incorrectAnswerTextField2.setVisible(true);

                incorrectAnswerLabel3.setVisible(true);
                incorrectAnswerTextField3.setVisible(true);
            }
            if(amount<=2)
            {
                incorrectAnswerLabel3.setVisible(false);
                incorrectAnswerTextField3.setVisible(false);
                incorrectAnswerTextField3.clear();
            }
            if(amount<=1)
            {
                incorrectAnswerLabel2.setVisible(false);
                incorrectAnswerTextField2.setVisible(false);
                incorrectAnswerTextField2.clear();
            }
        }
    }
}
