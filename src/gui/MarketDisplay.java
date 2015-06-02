/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import client.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import exception.DataValidationException;
import exception.InvalidPriceOperation;
import price.Price;
import price.PriceFactory;
import book.ProductService;
import publishers.MarketDataDTO;


/**
 *
 * @author hieldc
 */
public class MarketDisplay extends javax.swing.JFrame {

    private static double goodMarket = 0.10;
    private DefaultTableCellRenderer renderer = new MarketTableCellRenderer(goodMarket);
    private JPopupMenu popup;
    private int menuRow;
    private User user;
    private QuoteEntryDisplay quoteEntryDisplay = new QuoteEntryDisplay(this);
    private OrderEntryDisplay orderEntryDisplay = new OrderEntryDisplay(this);
    private OrderCancelDisplay orderCancelDisplay = new OrderCancelDisplay(this);
    private BookDepthDisplay bookDepthDisplay = new BookDepthDisplay(this);
    private PositionDisplay positionDisplay = new PositionDisplay();

    MarketDisplay(User u) {
        user = u;

        initComponents();

        userNameText.setText(user.getUserName());
        positionDisplay.setTitle("User: " + user.getUserName());

        makePopUpMenu();

        renderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < marketTable.getColumnCount(); i++) {
            marketTable.setDefaultRenderer(marketTable.getColumnClass(i), renderer);
        }
        // repaint to show table cell changes
        marketTable.updateUI();

        ((DefaultTableModel) marketTable.getModel()).setRowCount(0);
        symbolCombo.removeAllItems();
        symbolCombo.insertItemAt("", 0);
        for (int i = 0; i < user.getProductList().size(); i++) {
            symbolCombo.insertItemAt(user.getProductList().get(i), (i + 1));
        }
        symbolCombo.setSelectedIndex(0);
        try {
            stateText.setText(user.getMarketState());
        } catch (Exception ex) {
            Logger.getLogger(MarketDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }



        setTitle("Market Display: " + u.getUserName());

    }

    User getUser() {
        return user;
    }

    private void makePopUpMenu() {
        JMenuItem menuItem;

        popup = new JPopupMenu();
        menuItem = new JMenuItem("Enter Quote");
        menuItem.addActionListener(new MenuActionListener());
        popup.add(menuItem);

        menuItem = new JMenuItem("Cancel Quote");
        menuItem.addActionListener(new MenuActionListener());
        popup.add(menuItem);
        popup.add(new JSeparator()); // SEPARATOR

        menuItem = new JMenuItem("Enter Order");
        menuItem.addActionListener(new MenuActionListener());
        popup.add(menuItem);

        menuItem = new JMenuItem("Cancel Order");
        menuItem.addActionListener(new MenuActionListener());
        popup.add(menuItem);
        popup.add(new JSeparator()); // SEPARATOR

        menuItem = new JMenuItem("Display Book Depth");
        menuItem.addActionListener(new MenuActionListener());
        popup.add(menuItem);


        MouseListener popupListener = new PopupListener();
        marketTable.addMouseListener(popupListener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        userNameText = new javax.swing.JLabel();
        actionText = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        stateText = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        marketTable = new javax.swing.JTable();
        symbolCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tickerText = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        activityText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));

        jLabel2.setText("Username: ");

        jLabel3.setText("Last Action:");

        stateLabel.setText("Market State:");

        jButton1.setText("Position");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButton1ActionPerformed(evt);
				} catch (InvalidPriceOperation e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DataValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(userNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(stateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stateText, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(actionText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(userNameText, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(stateLabel, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(stateText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(actionText)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {actionText, userNameText});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Market"));

        marketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Symbol", "Buy Volume", "Buy Price", "Market Width", "Sell Price", "Sell Volume", "Last Sale"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(marketTable);

        symbolCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        symbolCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symbolComboActionPerformed(evt);
            }
        });

        jLabel1.setText("Select Symbol:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(symbolCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(symbolCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ticker"));

        tickerText.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        tickerText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tickerTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tickerText)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tickerText, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Market Activity"));

        activityText.setColumns(20);
        activityText.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        activityText.setRows(5);
        jScrollPane2.setViewportView(activityText);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tickerTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tickerTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tickerTextActionPerformed

    private void symbolComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symbolComboActionPerformed
        int indx = symbolCombo.getSelectedIndex();
        if (indx <= 0) {
            return;
        }
        String product = (String) symbolCombo.getSelectedItem();
        symbolCombo.removeItemAt(indx);
        try {
            MarketDataDTO md = ProductService.getInstance().getMarketData(product);
            md.buyPrice = (md.buyPrice == null ? PriceFactory.makeLimitPrice("0.00") : md.buyPrice);
            md.sellPrice = (md.sellPrice == null ? PriceFactory.makeLimitPrice("0.00") : md.sellPrice);

            String width;
            if (md.buyPrice.isMarket() || md.sellPrice.isMarket()) {
                width = "MKT";
            } else {
                width = md.sellPrice.subtract(md.buyPrice).toString();
            }

            String[] row = {product,
                "" + md.buyVolume,
                md.buyPrice.toString(),
                width,
                md.sellPrice.toString(),
                "" + md.sellVolume, ""};

            ((DefaultTableModel) marketTable.getModel()).addRow(row);

            symbolCombo.setSelectedIndex(0);

            user.subscribeCurrentMarket(product);
            user.subscribeLastSale(product);
            user.subscribeTicker(product);
            user.subscribeMessages(product);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_symbolComboActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws InvalidPriceOperation, DataValidationException {//GEN-FIRST:event_jButton1ActionPerformed
        positionDisplay.updateAccountBalance(user.getAccountCosts());
        positionDisplay.updateStockValue(user.getAllStockValue());
        positionDisplay.updateAccountValue(user.getNetAccountValue());

        ArrayList<String[]> positions = new ArrayList<>();
        for (String sym : user.getHoldings()) {
            String[] data = new String[4];
            data[0] = sym;
            data[1] = user.getStockPositionVolume(sym) > 0 ? "Bought" : "Sold";
            data[2] = "" + user.getStockPositionVolume(sym);
            data[3] = user.getStockPositionValue(sym).toString();
            positions.add(data);
        }
        positionDisplay.updatePositions(positions);
        positionDisplay.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    void updateMarketData(String product, Price bp, int bv, Price sp, int sv) throws InvalidPriceOperation {
        int row = getRowForProduct(product);
        if (row < 0) {
            return;
        }

        Price buyP = bp == null ? PriceFactory.makeLimitPrice("0.00") : bp;
        Price sellP = bp == null ? PriceFactory.makeLimitPrice("0.00") : sp;

        String width = "";
        try {
            width = sellP.subtract(buyP).toString();
        } catch (Exception ex) {
            Logger.getLogger(MarketDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }


        ((DefaultTableModel) marketTable.getModel()).setValueAt(bv, row, 1);
        ((DefaultTableModel) marketTable.getModel()).setValueAt(buyP.toString(), row, 2);
        ((DefaultTableModel) marketTable.getModel()).setValueAt(width, row, 3);
        ((DefaultTableModel) marketTable.getModel()).setValueAt(sellP.toString(), row, 4);
        ((DefaultTableModel) marketTable.getModel()).setValueAt(sv, row, 5);
    }

    void updateLastSale(String product, Price p, int v) throws InvalidPriceOperation {

        int row = getRowForProduct(product);
        if (row < 0) {
            return;
        }
        ((DefaultTableModel) marketTable.getModel()).setValueAt(
                v + "@" + (p == null ? PriceFactory.makeLimitPrice("0.00").toString() : p.toString()), row, 6);

    }

    void updateMarketActivity(String activity) {
        activityText.append(activity);
        activityText.setCaretPosition(activityText.getDocument().getLength());
    }

    void updateMarketState(String state) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        activityText.append(String.format("{" + t.toString() + "} Market State Changed to " + state + "%n"));
        activityText.setCaretPosition(activityText.getDocument().getLength());
        stateText.setText(state);

    }

    void updateTicker(String product, Price p, char direction) {

        int row = getRowForProduct(product);
        if (row < 0) {
            return;
        }

        String newData = " " + product + " " + p + direction + "    ";
        String s = tickerText.getText() + newData;

        int fieldWidth = tickerText.getWidth();
        int dataWidth = tickerText.getFontMetrics(tickerText.getFont()).stringWidth(s);

        if (dataWidth > fieldWidth) {
            s = s.substring(newData.length());
        }

        tickerText.setText(s);

    }

    private int getRowForProduct(String p) {
        int rows = ((DefaultTableModel) marketTable.getModel()).getRowCount();
        for (int i = 0; i < rows; i++) {
            if (((DefaultTableModel) marketTable.getModel()).getValueAt(i, 0).equals(p)) {
                return i;
            }
        }
        return -1;
    }

    private void showBookDepth(String product, String[][] bd) {
        StringBuilder sb = new StringBuilder();

        int max = Math.max(bd[0].length, bd[1].length);

        sb.append(String.format("%-30s%-25s%n", "BUY", "SELL"));
        for (int i = 0; i < max; i++) {
            sb.append(String.format("%-22s", bd[0].length > i ? bd[0][i] : ""));
            sb.append(String.format("%-25s%n", bd[1].length > i ? bd[1][i] : ""));
        }
        JOptionPane.showMessageDialog(this, sb, "Book Depth: " + product, JOptionPane.INFORMATION_MESSAGE);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actionText;
    private javax.swing.JTextArea activityText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable marketTable;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JLabel stateText;
    private javax.swing.JComboBox symbolCombo;
    private javax.swing.JTextField tickerText;
    private javax.swing.JLabel userNameText;
    // End of variables declaration//GEN-END:variables

    class MenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            Timestamp t = new Timestamp(System.currentTimeMillis());
            String product = (String) ((DefaultTableModel) marketTable.getModel()).getValueAt(menuRow, 0);
            try {
            switch (ae.getActionCommand()) {
                case "Enter Quote":
                    //System.out.println("Enter Quote for " + product);
                    actionText.setText("Enter Quote for " + product + " at " + t.toString());
                    quoteEntryDisplay.setVisible(product);
                    break;
                case "Cancel Quote":
                    //System.out.println("Cancel Quote for " + product);
                    actionText.setText("Cancel Quote for " + product + " at " + t.toString());
                    int i = JOptionPane.showConfirmDialog(null, "Cancel Quote for " + product + "?", "Cancel Quote", JOptionPane.OK_CANCEL_OPTION);
                    if (i != 0) {
                        return;
                    }
                    try {
                        user.submitQuoteCancel(product);
                    } catch (Exception ex) {
                        Logger.getLogger(MarketDisplay.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "Enter Order":
                    //System.out.println("Enter Order for " + product);
                    actionText.setText("Enter Order for " + product + " at " + t.toString());
                    orderEntryDisplay.setVisible(product);
                    break;
                case "Cancel Order":
                    //System.out.println("Cancel Order for " + product);
                    actionText.setText("Cancel Order for " + product + " at " + t.toString());
                    orderCancelDisplay.setVisible(product, user.getOrdersWithRemainingQty(product));
                    break;
                case "Display Book Depth":
                    //System.out.println("Display Book Depth for " + product);
                    actionText.setText("Display Book Depth for " + product + " at " + t.toString());
                    bookDepthDisplay.setVisible(product);

                    break;
            }
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            menuRow = marketTable.rowAtPoint(e.getPoint());
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
