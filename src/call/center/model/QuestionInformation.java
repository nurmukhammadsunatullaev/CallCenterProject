/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package call.center.model;

import call.center.controller.utils.MessageDialog;
import call.center.controller.utils.WidgetUtils;
import callcenter.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import call.center.controller.utils.lib.FXColumnTable;
import callcenter.util.IExcel;
import java.awt.Desktop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


@Entity
public class QuestionInformation implements Serializable,EventHandler<ActionEvent>,IExcel{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @FXColumnTable(columnName = "pdfButton")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="userId")
    @FXColumnTable
    private UserInformation operatorId;
  
    @ManyToOne
    @JoinColumn(name="loginId")
    @FXColumnTable
    private LoginInformation questionDate;
      
    
    @Column
    @FXColumnTable
    private String fullName;
    
    
    @Column(length = 512)
    @FXColumnTable
    private String address;
    
    
    @Column
    @FXColumnTable
    private String phoneNumber;
    
    @Column
    @FXColumnTable
    private String homePhoneNumber;
    
    @Column
    @FXColumnTable
    private String questionContent;
    
    @Column
    @FXColumnTable(columnName = "itemLetter", joinColumn = true,joinColumnName = "typeAnswer")
    private boolean byLetter;
    
    @Column
    @FXColumnTable(columnName = "itemEmail", joinColumn = true,joinColumnName = "typeAnswer")
    private boolean  byEmail;
    
    @Column
    @FXColumnTable(columnName = "itemPhone", joinColumn = true,joinColumnName = "typeAnswer")
    private boolean byPhone;

    @Transient
    private final Image selectedImage;

    @Transient
    private final Image notSelectedImage;
    
    public QuestionInformation() {
        selectedImage=new Image(getClass().getResourceAsStream("/resources/true.png"));
        notSelectedImage=new Image(getClass().getResourceAsStream("/resources/false.png"));
    }
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the operatorId
     */
    public UserInformation getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId the operatorId to set
     */
    public void setOperatorId(UserInformation operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the homePhoneNumber
     */
    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    /**
     * @param homePhoneNumber the homePhoneNumber to set
     */
    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    /**
     * @return the questionContent
     */
    public String getQuestionContent() {
        return questionContent;
    }

    /**
     * @param questionContent the questionContent to set
     */
    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    /**
     * @return the byLetter
     */
    public boolean isByLetter() {
        return byLetter;
    }

    /**
     * @param byLetter the byLetter to set
     */
    public void setByLetter(boolean byLetter) {
        this.byLetter = byLetter;
    }

    /**
     * @return the byEmail
     */
    public boolean isByEmail() {
        return byEmail;
    }

    /**
     * @param byEmail the byEmail to set
     */
    public void setByEmail(boolean byEmail) {
        this.byEmail = byEmail;
    }

    /**
     * @return the byPhone
     */
    public boolean isByPhone() {
        return byPhone;
    }

    /**
     * @param byPhone the byPhone to set
     */
    public void setByPhone(boolean byPhone) {
        this.byPhone = byPhone;
    }

    public LoginInformation getQuestionDate() {
        return questionDate;
    }
    
    public void setQuestionDate(LoginInformation questionDate) {
        this.questionDate = questionDate;
    }

    /**
     * @return the itemLetter
     */
   
    public ImageView getItemLetter() {
        return getFullImageView(byLetter);
    }
    
    private ImageView getFullImageView(boolean value){
        ImageView image=WidgetUtils.getCustomImageView(30, 30);
        image.setImage(value?selectedImage:notSelectedImage);
        return image;
    }

    /**
     * @return the itemEmail
     */
    public ImageView getItemEmail() {
       return getFullImageView(byEmail);
    }

    /**
     * @return the itemPhone
     */
    public ImageView getItemPhone() {
        return getFullImageView(byPhone);
    }

    /**
     * @return the pdfButton
     */
    public Button getPdfButton() {
        Button currentPdfButton=new Button("", WidgetUtils.getCustomImageView(50, 50, "/resources/pdf.png"));
        currentPdfButton.setOnAction(this);
        return currentPdfButton;
    }
    
    @Override
    public  String[] getHeaders(){
        ResourceBundle bundle=FileUtils.getCurrentResource();
        return new String[]{
            bundle.getString("fullName"),
            bundle.getString("address"),
            bundle.getString("phoneNumber"),
            bundle.getString("homePhoneNumber"),
            bundle.getString("questionContent"),
            bundle.getString( "itemLetter"), 
            bundle.getString( "itemEmail"), 
            bundle.getString( "itemPhone"),  
            bundle.getString( "operatorId"),     
            bundle.getString( "questionDate")
        };
    }
    
    @Override
    public String[] getFullData(){
        return new String[]{
            fullName,
            address,
            phoneNumber,
            homePhoneNumber,
            questionContent,
            typeAnswer(byLetter),
            typeAnswer(byEmail),
            typeAnswer(byPhone),
            operatorId.getFullName(),
            questionDate.getLoginDate().toString()
        };
    }
    
    private String typeAnswer(boolean type){
       return type ? "+" : "-";
    }
    
    @Override
    public void handle(ActionEvent event) {
        try {
            Platform.runLater(new SavePDF()); 
        } catch (IOException ex) {
            Logger.getLogger(QuestionInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    class SavePDF implements Runnable{
        private final PDDocument document=new PDDocument();
        private final PDPage page=new PDPage();
        private final PDPageContentStream stream;
        private final PDRectangle mediabox = page.getMediaBox();
        private final String fileName=FileUtils.ALL_PDF_FILES_FOLDER+questionDate+"-"+id+".pdf";
        float margin = 72;
        float width = mediabox.getWidth() - 2*margin;
        float startX = mediabox.getLowerLeftX() + margin;
        float startY = mediabox.getUpperRightY() - margin;
        float leading=-18;
        PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/resources/font/LiberationSans-Regular.ttf"));
        PDImageXObject header;
        public SavePDF() throws IOException {
            stream=new PDPageContentStream(document, page);
            header=PDImageXObject.createFromFile("header.jpg", document);
            document.addPage(page);
            stream.drawImage(header,5, startY-100,605,160);
            
        }
        
        
        private void createFile() throws IOException{
            stream.beginText();
            stream.setFont(font, 12);
            stream.newLineAtOffset(startX+30, startY-120);
            String questionNumber=(questionDate.getLoginDate()+"-"+id).replace('-', '_');
            stream.showText(questionNumber);
            stream.newLineAtOffset(startX+280, 0);
            writeLines(fullName,15);
            writeLines(address,15);
            stream.newLineAtOffset(0, leading);
            stream.showText(phoneNumber);
            stream.newLineAtOffset(0, leading);
            stream.showText(homePhoneNumber);
            stream.newLineAtOffset(-100, leading);
            stream.showText("МУРОЖАТ");
            stream.newLineAtOffset(-280, 2*leading);
            writeLines(questionContent,80);
            stream.newLineAtOffset(0, 4*leading);
            stream.showText("Ижрочи: "+operatorId.getFullName());
            stream.endText();
            stream.close();
            File file=new File(fileName);
            if(file.exists()){
                file.delete();
            }
            document.save(file);
            document.close();
        }
        private void writeLines(String content,int len) throws IOException{
            List<String> lines=getLines(content,len);
            for(String line:lines){
               stream.showText(line);
               stream.newLineAtOffset(0, leading);
            }   
        }
        
        private List<String> getLines(String content,int length){
            String [] words=getWords(content);
            ArrayList<String> lines=new ArrayList<>();
            StringBuilder builder=new StringBuilder("   ");
            for (String word : words) {
                if (builder.length()+word.length()<length) {
                    builder.append(word).append(" ");
                } else {  
                    builder.trimToSize();
                    builder.deleteCharAt(builder.length()-1);
                    lines.add(builder.toString());
                    builder.setLength(0);
                    builder.append(word).append(" ");
                }
            }
            if(builder.length()!=0){
              lines.add(builder.toString());
            }
            return lines;
        }
        
        private String [] getWords(String content){
            String [] words = content.split("\\s+|,\\s*");
            return words!=null ? words : new String[]{content};
        }
        
        @Override
        public void run() {
            try {
                createFile();
                Desktop.getDesktop().open(new File(fileName));
            } catch (IOException ex) {
                MessageDialog.show("#", "#", "#"+fileName);
                Logger.getLogger(QuestionInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}

