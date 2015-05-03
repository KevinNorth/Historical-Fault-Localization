package edu.unl.knorth.historical_fault_localization.visualizer;

import edu.unl.knorth.historical_fault_localization.intermediate_data.StatementData;
import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SuspiciousnessVisualizer {
    /**
     * Creates a series of PNG files that represent the suspiciousness scores
     * for each of the commits analyzed. Each statement is represented as a
     * horizontal bar of color. Each file is represented by a vertical stack of
     * these bars, with the top statements being the first lines in the file.
     * @param statementHeight The number of pixels tall that each statement's
     * bar of color should be.
     * @param statementWidth The number of pixels wide that each statement's bar
     * of color should be.
     * @param fileMargin The number of pixels between stacks representing files.
     * @param fileFontSize The size of the font labeling each file.
     * @param outputDirectory The directory to save all of the PNG files to.
     * This should be a string that does NOT end in a "/".
     * @param suspiciousnessScores The suspiciousness scores to visualize.
     */
    public void visualizeSuspiciousnessForAllCommits(int statementHeight,
            int statementWidth, int fileMargin, int fileFontSize,
            String outputDirectory, List<SuspiciousnessScoresFromCommit>
                    suspiciousnessScores) {
        for(SuspiciousnessScoresFromCommit ssfc : suspiciousnessScores) {
            try {
            visualizeSuspicousnessForOneCommit(statementHeight, statementWidth,
                    fileMargin, fileFontSize, outputDirectory, ssfc);
            } catch(IOException err) {
                System.out.println("Could not visualize commit #"
                        + ssfc.getOrderProcessed() + " (hash: "
                        + ssfc.getCommitHash() + ") due to error:");
                err.printStackTrace(System.out);
            }
        }
    }
    
    /**
     * Creates an image for a single commit.
     * @param statementHeight The number of pixels tall that each statement's
     * bar of color should be.
     * @param statementWidth The number of pixels wide that each statement's bar
     * of color should be.
     * @param fileMargin The number of pixels between stacks representing files.
     * @param fileFontSize The size of the font labeling each file.
     * @param outputDirectory The directory to save all of the PNG files to.
     * This should be a string that does NOT end in a "/".
     * @param suspiciousnessScores The suspiciousness scores to visualize.
     * @throws IOException If there was a problem saving the file.
     */
    private void visualizeSuspicousnessForOneCommit(int statementHeight,
            int statementWidth, int fileMargin, int fileFontSize,
            String outputDirectory,
            SuspiciousnessScoresFromCommit suspiciousnessScores)
            throws IOException {
        int maxLineNumber = getMaxLineNumber(suspiciousnessScores);
        List<String> files = getFiles(suspiciousnessScores);
        
        int imageWidth = (fileMargin * (files.size() - 1)) +
                (statementWidth * files.size());
        int imageHeight = (maxLineNumber * statementHeight) + (2*fileFontSize);
        
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
            BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();
        
        // Make the barckground transparent
        g2.setBackground(new Color(0, 0, 0, 0));
        
        // Draw bars for individual statements
        for(SuspiciousnessScore ss
                : suspiciousnessScores.getSuspiciousnessScores()) {
            int lineNumber = ss.getStatement().getLineNumber();
            int fileNumber = files.indexOf(ss.getStatement().getFile());
            
            Rectangle rect = determineStatementRectange(fileNumber, lineNumber,
                    statementHeight, statementWidth, fileMargin, fileFontSize);
            
            double suspiciousness = ss.getSuspiciousness();
            float hue = (float) ((1 - suspiciousness) * (240.0 / 360.0)); // Goes from red to green
            
            g2.setColor(new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f)));
            g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        
        // Fill in missing statements with black
        List<StatementData> missingStatements =
                getMissingStatements(suspiciousnessScores, files);
        g2.setColor(Color.BLACK);
        for(StatementData statement : missingStatements) {
            int lineNumber = statement.getLineNumber();
            int fileNumber = files.indexOf(statement.getFile());
            
            Rectangle rect = determineStatementRectange(fileNumber, lineNumber,
                    statementHeight, statementWidth, fileMargin, fileFontSize);

            g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        
        // Add file headers
        int fileIndex = 0;
        g2.setFont(new Font("Arial", Font.PLAIN, fileFontSize));
        for(String file : files) {
            g2.drawString(file, (statementWidth + fileMargin) * fileIndex,
                    fileFontSize);
            fileIndex++;
        }
        
        // Save image
        String outputFilePath = outputDirectory + "/"
                + suspiciousnessScores.getOrderProcessed() + "-"
                + suspiciousnessScores.getCommitHash() + ".png";
        ImageIO.write(image, "png", new File(outputFilePath));
    }
    
    /**
     * Gets the largest line number recorded for a set of SuspiciousnessScores.
     */
    private int getMaxLineNumber(SuspiciousnessScoresFromCommit
            suspiciousnessScores) {
        int max = 0;
        for(SuspiciousnessScore ss
                : suspiciousnessScores.getSuspiciousnessScores()) {
            int lineNumber = ss.getStatement().getLineNumber();
            if(lineNumber > max) {
                max = lineNumber;
            }
        }
        
        return max;
    }

    /**
     * Gets the largest line number recorded for a particular file in a set of
     * SuspiciousnessScores.
     */
    private int getMaxLineNumberForFile(SuspiciousnessScoresFromCommit
            suspiciousnessScores, String file) {
        int max = 0;
        for(SuspiciousnessScore ss
                : suspiciousnessScores.getSuspiciousnessScores()) {
            int lineNumber = ss.getStatement().getLineNumber();
            String thisFile = ss.getStatement().getFile();
            if((lineNumber > max) && (file.equals(thisFile))) {
                max = lineNumber;
            }
        }
        
        return max;
    }
    
    /**
     * Gets a list of all of the unique files appearing in a set of
     * SuspiciousnessScores.
     */
    private List<String> getFiles(SuspiciousnessScoresFromCommit
            suspiciousnessScores) {
        List<String> fileNames = new LinkedList<>();
        
        for(SuspiciousnessScore ss
                : suspiciousnessScores.getSuspiciousnessScores()) {
            String newFileName = ss.getStatement().getFile();
            
            boolean match = false;
            for(String fileName : fileNames) {
                if(newFileName.equals(fileName)) {
                    match = true;
                    break;
                }
            }
            
            if(!match) {
                fileNames.add(newFileName);
            }
        }
        
        return fileNames;
    }
    
    /**
     * Generates a list of all of the file/line number combinations that aren't
     * present in a set of SuspiciousnessScores.
     */
    private List<StatementData> getMissingStatements(
            SuspiciousnessScoresFromCommit suspiciousnessScoresFromCommit,
            List<String> files) {
        List<SuspiciousnessScore> suspiciousnessScores =
                suspiciousnessScoresFromCommit.getSuspiciousnessScores();
        
        List<StatementData> missingStatements = new LinkedList<>();
        
        for(String file : files) {
            int maxLineNumber =
                    getMaxLineNumberForFile(suspiciousnessScoresFromCommit,
                            file);
            
            for(int lineNumber = 1; lineNumber <= maxLineNumber; lineNumber++) {
                StatementData missingStatement =
                        new StatementData(lineNumber, file);
                
                boolean match = false;
                for(SuspiciousnessScore ss : suspiciousnessScores) {
                    if(missingStatement.equals(ss.getStatement())) {
                        match = true;
                        break;
                    }
                }
                
                if(!match) {
                    missingStatements.add(missingStatement);
                }
            }
        }
        
        return missingStatements;
    }
    
    /**
     * Determines where a bar of color representing a statement's suspiciousness
     * should be drawn.
     * @param fileNumber Indicates which file stack the bar should be drawn in.
     * @param lineNumber The statement's line number.
     * @param statementHeight The number of pixels tall that each statement's
     * bar of color should be.
     * @param statementWidth The number of pixels wide that each statement's bar
     * of color should be.
     * @param fileMargin The number of pixels between stacks representing files.
     * @param fileFontSize The size of the font labeling each file.
     * @return A Rectangle indicating where the bar of color should be drawn.
     */
    private Rectangle determineStatementRectange(int fileNumber,
            int lineNumber, int statementHeight, int statementWidth,
            int fileMargin, int fileFontSize) {
        int top = ((lineNumber - 1) * statementHeight) + fileFontSize;
        int bottom = (lineNumber * statementHeight) + fileFontSize;
        int left = (fileNumber * (statementWidth + fileMargin));
        int right = left + statementWidth;
        
        return new Rectangle(left, top, right - left, bottom - top);
    }
}