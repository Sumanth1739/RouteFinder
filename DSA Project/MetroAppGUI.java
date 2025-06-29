import java.awt.*;
import java.util.*;
import javax.swing.*;

public class MetroAppGUI {
    public static void main(String[] args) {
        Graph_M metroGraph = new Graph_M();
        Graph_M.Create_Hyderabad_Map(metroGraph);
        System.out.println("✅ Total Stations Loaded:" + Graph_M.vtces.size());

        // Extract clean station names for display
        Map<String, String> stationDisplayMap = new LinkedHashMap<>();
        for (String key : Graph_M.vtces.keySet()) {
            String displayName = key.contains("~") ? key.substring(0, key.indexOf("~")) : key;
            if (!stationDisplayMap.containsKey(displayName)) {
                stationDisplayMap.put(displayName, key); // map clean name to full code
            }
        }

        JFrame frame = new JFrame("Hyderabad Metro Journey Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel srcLabel = new JLabel("Source Station:");
        JLabel destLabel = new JLabel("Destination Station:");

        String[] displayNames = stationDisplayMap.keySet().toArray(new String[0]);
        System.out.println("Display names count: " + displayNames.length);  // Debugging

        JComboBox<String> srcBox = new JComboBox<>(displayNames);
        JComboBox<String> destBox = new JComboBox<>(displayNames);

        // ✅ Set visible dropdown rows to 20
        srcBox.setMaximumRowCount(20);
        destBox.setMaximumRowCount(20);

        JButton distBtn = new JButton("Find Shortest Distance");
        JButton timeBtn = new JButton("Find Shortest Time");

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);

        distBtn.addActionListener(e -> {
            String srcDisplay = (String) srcBox.getSelectedItem();
            String dstDisplay = (String) destBox.getSelectedItem();
            String src = stationDisplayMap.get(srcDisplay);
            String dst = stationDisplayMap.get(dstDisplay);
            int dist = metroGraph.dijkstra(src, dst, false);
            resultArea.setText("Shortest Distance from " + srcDisplay + " to " + dstDisplay + " is " + dist + " KM");
        });

        timeBtn.addActionListener(e -> {
            String srcDisplay = (String) srcBox.getSelectedItem();
            String dstDisplay = (String) destBox.getSelectedItem();
            String src = stationDisplayMap.get(srcDisplay);
            String dst = stationDisplayMap.get(dstDisplay);
            int timeInSec = metroGraph.dijkstra(src, dst, true);
            int minutes = (int) Math.ceil(timeInSec / 60.0);
            resultArea.setText("Shortest Time from " + srcDisplay + " to " + dstDisplay + " is " + minutes + " Minutes");
        });

        panel.add(srcLabel);
        panel.add(srcBox);
        panel.add(destLabel);
        panel.add(destBox);
        panel.add(distBtn);
        panel.add(timeBtn);

        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, scroll);

        frame.setVisible(true);
    }
}
