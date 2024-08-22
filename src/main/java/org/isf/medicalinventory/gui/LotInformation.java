/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2024 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.medicalinventory.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.isf.generaldata.MessageBundle;
import org.isf.medtype.manager.MedicalTypeBrowserManager;
import org.isf.medtype.model.MedicalType;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

public class LotInformation extends ModalJFrame {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel mainPanel;
	JButton jButtonCancel;
	JButton jButtonOk;
	JRadioButton all;
	JRadioButton onlyNonZero;
	JRadioButton withMovement;
	MedicalType medicalTypeSelected;
	JComboBox<MedicalType> medicalTypeComboBox;
	
	private MedicalTypeBrowserManager medicalTypeManager = Context.getApplicationContext().getBean(MedicalTypeBrowserManager.class);
	// Constructeur de la classe LotInformation
    public LotInformation() {
        setTitle(MessageBundle.getMessage("angal.inventoryrow.lotinformation.title"));
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre
        
        // Création du panneau principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); // Espacement entre les composants
        
        // Panneau pour la liste déroulante
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        leftPanel.setLayout(new GridLayout(3,1));
        leftPanel.add(new JLabel(MessageBundle.getMessage("angal.inventoryrow.medicaltype.txt")));
        leftPanel.add(getJComboMedicalType());
        
        // Grouper les boutons radio pour qu'une seule option puisse être sélectionnée
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(all);
        radioGroup.add(onlyNonZero);
        radioGroup.add(withMovement);
        
        // Panneau pour les boutons radio
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 1, 5, 5)); // 3 lignes, 1 colonne
        rightPanel.add(getAllRadioButton());
        rightPanel.add(getMedicalWithNonZeroQuatityRadioButton());
        rightPanel.add(getMedicalWithMovementRadioButton());
        
        // Panneau pour les boutons OK et Cancel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(getOkButton());
        bottomPanel.add(getCancelButton());
        
        // Ajouter les panneaux au panneau principal
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Ajouter le panneau principal à la fenêtre
        getContentPane().add(mainPanel);
        setVisible(true);
    }
    
    private void InitComponent() {
    	
    }
    
    private JComboBox<MedicalType> getJComboMedicalType() {
		if (medicalTypeComboBox == null) {
			medicalTypeComboBox = new JComboBox<MedicalType>();
			try {
				List<MedicalType> medicalTypes = medicalTypeManager.getMedicalType();	
				MedicalType medicalType = new MedicalType(MessageBundle.getMessage("angal.common.all.txt"), MessageBundle.getMessage("angal.common.all.txt"));
				medicalTypeComboBox.addItem(medicalType);
				for (MedicalType medType: medicalTypes) {
					medicalTypeComboBox.addItem(medType);
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
			
			medicalTypeComboBox.addActionListener(actionEvent -> {
				medicalTypeSelected = (MedicalType) medicalTypeComboBox.getSelectedItem();
			});
		}
		return medicalTypeComboBox;
	}
    
    private JButton getCancelButton() {
    	if (jButtonCancel == null) {
    		jButtonCancel = new JButton(MessageBundle.getMessage("angal.common.cancel.btn"));
    		jButtonCancel.setMnemonic(MessageBundle.getMnemonic("angal.common.cancel.btn.key"));
    	}
		jButtonCancel.addActionListener(actionEvent -> {
			dispose();
		});
		return jButtonCancel;
	}
    
    private JButton getOkButton() {
    	if (jButtonOk == null) {
    		jButtonOk = new JButton(MessageBundle.getMessage("angal.common.ok.btn"));
    		jButtonOk.setMnemonic(MessageBundle.getMnemonic("angal.common.ok.btn.key"));
    	}
    	jButtonOk.addActionListener(actionEvent -> {
			
		});
		return jButtonOk;
	}
    private JRadioButton getAllRadioButton() {
    	if (all == null) {
    		all = new JRadioButton(MessageBundle.getMessage("angal.common.all.btn"));
    		all.setMnemonic(MessageBundle.getMnemonic("angal.common.all.btn.key"));
    		all.setSelected(true);
    	}
    	all.addActionListener(actionEvent -> {
    		if (all.isSelected()) {
    			onlyNonZero.setSelected(false);
				withMovement.setSelected(false);
			}
		});
		return all;
	}
    
    private JRadioButton getMedicalWithNonZeroQuatityRadioButton() {
    	if (onlyNonZero == null) {
    		onlyNonZero = new JRadioButton(MessageBundle.getMessage("angal.inventory.medicalwithonlynonzeroqty.btn"));
    		onlyNonZero.setMnemonic(MessageBundle.getMnemonic("angal.inventory.medicalwithonlynonzeroqty.btn.key"));
    	}
    	onlyNonZero.addActionListener(actionEvent -> {
			if (onlyNonZero.isSelected()) {
				all.setSelected(false);
				withMovement.setSelected(false);
			}
		});
		return onlyNonZero;
	}
    
    private JRadioButton getMedicalWithMovementRadioButton() {
    	if (withMovement == null) {
    		withMovement = new JRadioButton(MessageBundle.getMessage("angal.inventory.medicalwithmovementonly.btn"));
    		withMovement.setMnemonic(MessageBundle.getMnemonic("angal.inventory.medicalwithmovementonly.btn.key"));
    	}
    	withMovement.addActionListener(actionEvent -> {
    		if (onlyNonZero.isSelected()) {
				all.setSelected(false);
				onlyNonZero.setSelected(false);
			}
		});
		return withMovement;
	}
}
