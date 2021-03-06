/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JOptionPane;

import price.Price;
import price.PriceFactory;
import tradable.BookSide;


/**
 *
 * @author hieldc
 */
public class OrderEntryDisplay extends javax.swing.JFrame {

    private MarketDisplay marketDisplay;
    private String product;

    /**
     * Creates new form QuoteEntryDisplay
     */
    public OrderEntryDisplay(MarketDisplay md) {
        initComponents();
        marketDisplay = md;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        orderVolume = new javax.swing.JTextField();
        orderPrice = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        mktCheck = new javax.swing.JCheckBox();
        buyRadio = new javax.swing.JRadioButton();
        sellRadio = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Entry"));

        jLabel1.setText("Order Price:");

        jLabel2.setText("Order Volume:");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton1.setText("Submit");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        mktCheck.setText("MKT");
        mktCheck.setFocusable(false);
        mktCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mktCheckActionPerformed(evt);
            }
        });

        buyRadio.setText("BUY");
        buyRadio.setFocusable(false);
        buyRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyRadioActionPerformed(evt);
            }
        });

        sellRadio.setText("SELL");
        sellRadio.setFocusable(false);
        sellRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellRadioActionPerformed(evt);
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
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mktCheck)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buyRadio)
                    .addComponent(sellRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {orderPrice, orderVolume});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(orderPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(mktCheck)
                    .addComponent(buyRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton2)
                    .addComponent(orderVolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(sellRadio))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jSeparator2)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setVisible(String p) {
        product = p;
        orderPrice.setText("");
        orderVolume.setText("");
        buyRadio.setSelected(false);
        sellRadio.setSelected(false);

        setTitle("Order Entry for " + product);
        super.setVisible(true);

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            if (orderPrice.getText().trim().isEmpty() && !mktCheck.isSelected()) {
                JOptionPane.showMessageDialog(this, "Buy price cannot be empty if price is not MKT", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (orderVolume.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Buy Volume cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (!buyRadio.isSelected() && !sellRadio.isSelected())
            {
                JOptionPane.showMessageDialog(this, "Buy or Sell must be selected", "Error", JOptionPane.ERROR_MESSAGE);
            }


            int v = 0;
            String field = "";
            String value = "";
            String buyPrice = "";
            try {
                if (!mktCheck.isSelected()) {
                    buyPrice = fixPrice(orderPrice.getText());
                    field = "Price";
                    value = orderPrice.getText();
                }
                field = "Volume";
                value = orderVolume.getText();
                v = Integer.parseInt(orderVolume.getText());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Numeric value for " + field + " field: " + value, "Invalid Numeric Data", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BookSide side = (buyRadio.isSelected() ? BookSide.BUY : BookSide.SELL);  // This should match your format for storing a side - enum, String, etc

            Price price = mktCheck.isSelected() ? PriceFactory.makeMarketPrice() : PriceFactory.makeLimitPrice(buyPrice);
            marketDisplay.getUser().submitOrder(product, price, v, side);

            setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private String fixPrice(String p) {
        String result = p;
        if (!result.contains(".")) {
            result += ".00";
        }
        String x = result.substring(result.indexOf("."));

        if (result.substring(result.indexOf(".")).length() == 2) {
            result += "0";
        }

        return result;
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void buyRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyRadioActionPerformed
        sellRadio.setSelected(false);
    }//GEN-LAST:event_buyRadioActionPerformed

    private void sellRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellRadioActionPerformed
        buyRadio.setSelected(false);
    }//GEN-LAST:event_sellRadioActionPerformed

    private void mktCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mktCheckActionPerformed
        if (mktCheck.isSelected())
            orderPrice.setEnabled(false);
        else
            orderPrice.setEnabled(true);
    }//GEN-LAST:event_mktCheckActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton buyRadio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JCheckBox mktCheck;
    private javax.swing.JTextField orderPrice;
    private javax.swing.JTextField orderVolume;
    private javax.swing.JRadioButton sellRadio;
    // End of variables declaration//GEN-END:variables
}
