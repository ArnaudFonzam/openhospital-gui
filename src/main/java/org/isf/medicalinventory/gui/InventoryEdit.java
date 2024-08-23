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

import static org.isf.utils.Constants.DATE_TIME_FORMATTER;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicalinventory.manager.MedicalInventoryManager;
import org.isf.medicalinventory.manager.MedicalInventoryRowManager;
import org.isf.medicalinventory.model.InventoryStatus;
import org.isf.medicalinventory.model.InventoryType;
import org.isf.medicalinventory.model.MedicalInventory;
import org.isf.medicalinventory.model.MedicalInventoryRow;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.manager.MovStockInsertingManager;
import org.isf.medicalstock.model.Lot;
import org.isf.medstockmovtype.manager.MedicalDsrStockMovementTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medtype.manager.MedicalTypeBrowserManager;
import org.isf.medtype.model.MedicalType;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.supplier.manager.SupplierBrowserManager;
import org.isf.supplier.model.Supplier;
import org.isf.utils.db.NormalizeString;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.GoodDateChooser;
import org.isf.utils.jobjects.GoodDateTimeSpinnerChooser;
import org.isf.utils.jobjects.MessageDialog;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.RequestFocusListener;
import org.isf.utils.jobjects.TextPrompt;
import org.isf.utils.jobjects.TextPrompt.Show;
import org.isf.utils.time.TimeTools;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;

public class InventoryEdit extends ModalJFrame {

	private static final long serialVersionUID = 1L;

	private static EventListenerList InventoryListeners = new EventListenerList();

	public interface InventoryListener extends EventListener {

		public void InventoryInserted(AWTEvent e);

		public void InventoryUpdated(AWTEvent e);

		public void InventoryCancelled(AWTEvent e);
	}

	public static void addInventoryListener(InventoryListener l) {
		InventoryListeners.add(InventoryListener.class, l);
	}

	public static void removeInventoryListener(InventoryListener listener) {
		InventoryListeners.remove(InventoryListener.class, listener);
	}

	private void fireInventoryInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {
			private static final long serialVersionUID = 1L;
		};
		
		EventListener[] listeners = InventoryListeners.getListeners(InventoryListener.class);
		for (int i = 0; i < listeners.length; i++) {
			((InventoryListener) listeners[i]).InventoryInserted(event);
		}
		jTableInventoryRow.updateUI();
	}

	private void fireInventoryUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {
			private static final long serialVersionUID = 1L;
		};
		
		EventListener[] listeners = InventoryListeners.getListeners(InventoryListener.class);
		for (int i = 0; i < listeners.length; i++) {
			((InventoryListener) listeners[i]).InventoryUpdated(event);
		}
		jTableInventoryRow.updateUI();	
	}
	
	private GoodDateChooser jCalendarInventory;
	private LocalDateTime dateInventory = TimeTools.getNow();
	private JPanel panelHeader;
	private JPanel panelFooter;
	private JPanel panelContent;
	private JButton closeButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton resetButton;
	private JButton lotButton;
	private JScrollPane scrollPaneInventory;
	private JTable jTableInventoryRow;
	private List<MedicalInventoryRow> inventoryRowList;
	private List<MedicalInventoryRow> inventoryRowSearchList = new ArrayList<>();
	private List<MedicalInventoryRow> inventoryRowListAdded = new ArrayList<>();
	private List<Lot> lotsSaved = new ArrayList<>();
	private HashMap<Integer, Lot> lotsDeleted = new HashMap<>();
	List<MedicalInventoryRow> inventoryRowsToDelete = new ArrayList<>();
	private String[] pColums = { MessageBundle.getMessage("angal.inventory.id.col").toUpperCase(),
			MessageBundle.getMessage("angal.common.code.txt").toUpperCase(),
			MessageBundle.getMessage("angal.inventory.product.col").toUpperCase(),
			MessageBundle.getMessage("angal.inventory.new.col").toUpperCase(),
			MessageBundle.getMessage("angal.wardpharmacy.lotnumber.col").toUpperCase(),
			MessageBundle.getMessage("angal.medicalstock.duedate.col").toUpperCase(),
			MessageBundle.getMessage("angal.inventoryrow.theoreticqty.col").toUpperCase(),
			MessageBundle.getMessage("angal.inventoryrow.realqty.col").toUpperCase(),
			MessageBundle.getMessage("angal.inventoryrow.unitprice.col").toUpperCase(),
			MessageBundle.getMessage("angal.inventory.totalprice").toUpperCase() };
	private int[] pColumwidth = { 50, 50, 200, 100, 100, 100, 100, 80, 80, 80 };
	private boolean[] columnEditable = { false, false, false, false, false, false, false, true, false, false };
	private boolean[] columnEditableView = { false, false, false, false, false, false, false, false, false, false };
	private boolean[] pColumnVisible = { false, true, true, true, !GeneralData.AUTOMATICLOT_IN, true, true, true, GeneralData.LOTWITHCOST, GeneralData.LOTWITHCOST };
	private MedicalInventory inventory = null;
	private JRadioButton specificRadio;
	private JRadioButton allRadio;
	private JLabel dateInventoryLabel;
	private JTextField codeTextField;
	private String code = null;
	private String mode = null;
	private JLabel referenceLabel;
	private JLabel chargeTypeLabel;
	private JLabel dischargeTypeLabel;
	private JLabel supplierLabel;
	private JLabel destinationLabel;
	private JTextField referenceTextField;
	private JTextField jTetFieldEditor;
	private JComboBox<MovementType> chargeCombo;
	private JComboBox<MovementType> dischargeCombo;
	private JComboBox<Supplier> supplierCombo;
	private JComboBox<Ward> destinationCombo;
	private MovementType chargeType = null;
	private MovementType dischargeType = null;
	private Supplier supplier = null;
	private Ward destination = null;
	private boolean selectAll = false;
	private String newReference = null;
	private JButton selectButton;
	private JFrame frame;
	private JPanel mainPanel;
	private JRadioButton all;
	private JRadioButton onlyNonZero;
	private JRadioButton withMovement;
	private JButton jButtonCancel;
	private JButton jButtonOk;
	private MedicalType medicalTypeSelected;
	private JComboBox<MedicalType> medicalTypeComboBox;
	
	private MedicalTypeBrowserManager medicalTypeManager = Context.getApplicationContext().getBean(MedicalTypeBrowserManager.class);
	private MedicalInventoryManager medicalInventoryManager = Context.getApplicationContext().getBean(MedicalInventoryManager.class);
	private MedicalInventoryRowManager medicalInventoryRowManager = Context.getApplicationContext().getBean(MedicalInventoryRowManager.class);
	private MedicalBrowsingManager medicalBrowsingManager = Context.getApplicationContext().getBean(MedicalBrowsingManager.class);
	private MovStockInsertingManager movStockInsertingManager = Context.getApplicationContext().getBean(MovStockInsertingManager.class);
	private MedicalDsrStockMovementTypeBrowserManager movTypeManager = Context.getApplicationContext().getBean(MedicalDsrStockMovementTypeBrowserManager.class);
	private SupplierBrowserManager supplierManager = Context.getApplicationContext().getBean(SupplierBrowserManager.class);
	private WardBrowserManager wardManager = Context.getApplicationContext().getBean(WardBrowserManager.class);
	
	public InventoryEdit() {
		mode = "new";
		initComponents();
	}
	
	private boolean isAutomaticLotIn() {
		return GeneralData.AUTOMATICLOT_IN;
	}
	
	public InventoryEdit(MedicalInventory inventory, String mod) {
		this.inventory = inventory;
		mode = mod;
		initComponents();
	}

	private void initComponents() {
		inventoryRowList = new ArrayList<>();
		inventoryRowSearchList = new ArrayList<>();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(1000, 600));
		setLocationRelativeTo(null);
		if (mode.equals("new")) {
			setTitle(MessageBundle.getMessage("angal.inventory.newinventory.title"));
		} 
		if (mode.equals("view")) {
			setTitle(MessageBundle.getMessage("angal.inventory.viewinventory.title"));
		} 
		if (mode.equals("update")) {
			setTitle(MessageBundle.getMessage("angal.inventory.editinventory.title"));
		}
		getContentPane().setLayout(new BorderLayout());

		panelHeader = getPanelHeader();

		getContentPane().add(panelHeader, BorderLayout.NORTH);

		panelContent = getPanelContent();
		getContentPane().add(panelContent, BorderLayout.CENTER);

		panelFooter = getPanelFooter();
		getContentPane().add(panelFooter, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				closeButton.doClick();
			}
		});
		if (mode.equals("view")) {
			saveButton.setVisible(false);
			deleteButton.setVisible(false);
			columnEditable = columnEditableView;
			codeTextField.setEditable(false);
			resetButton.setVisible(false);
			referenceTextField.setEditable(false);
			jCalendarInventory.setEnabled(false);
			specificRadio.setEnabled(false);
			//allRadio.setEnabled(false);
			chargeCombo.setEnabled(false);
			dischargeCombo.setEnabled(false);
			supplierCombo.setEnabled(false);
			destinationCombo.setEnabled(false);
			lotButton.setVisible(false);
		} else {
			saveButton.setVisible(true);
			deleteButton.setVisible(true);
			codeTextField.setEditable(true);
			resetButton.setVisible(true);
			referenceTextField.setEditable(true);
			jCalendarInventory.setEnabled(true);
			specificRadio.setEnabled(true);
			//allRadio.setEnabled(true);
			chargeCombo.setEnabled(true);
			dischargeCombo.setEnabled(true);
			supplierCombo.setEnabled(true);
			destinationCombo.setEnabled(true);
			lotButton.setVisible(true);
		}
	}

	private JPanel getPanelHeader() {
		if (panelHeader == null) {
			panelHeader = new JPanel();
			panelHeader.setBorder(new EmptyBorder(5, 0, 5, 0));
			GridBagLayout gbl_panelHeader = new GridBagLayout();
			gbl_panelHeader.columnWidths = new int[] { 159, 191, 192, 218, 51, 0 };
			gbl_panelHeader.rowHeights = new int[] { 30, 30, 0 };
			gbl_panelHeader.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_panelHeader.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			panelHeader.setLayout(gbl_panelHeader);
			GridBagConstraints gbc_dateInventoryLabel = new GridBagConstraints();
			gbc_dateInventoryLabel.insets = new Insets(0, 0, 5, 5);
			gbc_dateInventoryLabel.gridx = 0;
			gbc_dateInventoryLabel.gridy = 0;
			panelHeader.add(getDateInventoryLabel(), gbc_dateInventoryLabel);

			GridBagConstraints gbc_jCalendarInventory = new GridBagConstraints();
			gbc_jCalendarInventory.fill = GridBagConstraints.HORIZONTAL;
			gbc_jCalendarInventory.insets = new Insets(0, 0, 5, 5);
			gbc_jCalendarInventory.gridx = 1;
			gbc_jCalendarInventory.gridy = 0;
			panelHeader.add(getJCalendarFrom(), gbc_jCalendarInventory);
			GridBagConstraints gbc_referenceLabel = new GridBagConstraints();
			gbc_referenceLabel.anchor = GridBagConstraints.EAST;
			gbc_referenceLabel.insets = new Insets(0, 0, 5, 5);
			gbc_referenceLabel.gridx = 2;
			gbc_referenceLabel.gridy = 0;
			panelHeader.add(getReferenceLabel(), gbc_referenceLabel);
			GridBagConstraints gbc_referenceTextField = new GridBagConstraints();
			gbc_referenceTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_referenceTextField.insets = new Insets(0, 0, 5, 5);
			gbc_referenceTextField.gridx = 3;
			gbc_referenceTextField.gridy = 0;
			panelHeader.add(getReferenceTextField(), gbc_referenceTextField);
			GridBagConstraints gbc_chargeLabel = new GridBagConstraints();
			gbc_chargeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_chargeLabel.gridx = 0;
			gbc_chargeLabel.gridy = 1;
			panelHeader.add(getChargeLabel(), gbc_chargeLabel);
			GridBagConstraints gbc_jComboCharge = new GridBagConstraints();
			gbc_jComboCharge.fill = GridBagConstraints.HORIZONTAL;
			gbc_jComboCharge.insets = new Insets(0, 0, 5, 5);
			gbc_jComboCharge.gridx = 1;
			gbc_jComboCharge.gridy = 1;
			panelHeader.add(getJComboCharge(), gbc_jComboCharge);
			GridBagConstraints gbc_supplierLabel = new GridBagConstraints();
			gbc_supplierLabel.anchor = GridBagConstraints.EAST;
			gbc_supplierLabel.insets = new Insets(0, 0, 5, 5);
			gbc_supplierLabel.gridx = 2;
			gbc_supplierLabel.gridy = 1;
			panelHeader.add(getSupplierLabel(), gbc_supplierLabel);
			GridBagConstraints gbc_supplierCombo = new GridBagConstraints();
			gbc_supplierCombo.fill = GridBagConstraints.HORIZONTAL;
			gbc_supplierCombo.insets = new Insets(0, 0, 5, 5);
			gbc_supplierCombo.gridx = 3;
			gbc_supplierCombo.gridy = 1;
			panelHeader.add(getJComboSupplier(), gbc_supplierCombo);
			GridBagConstraints gbc_dischargeLabel = new GridBagConstraints();
			gbc_dischargeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_dischargeLabel.gridx = 0;
			gbc_dischargeLabel.gridy = 2;
			panelHeader.add(getDischargeLabel(), gbc_dischargeLabel);
			GridBagConstraints gbc_dichargeCombo = new GridBagConstraints();
			gbc_dichargeCombo.fill = GridBagConstraints.HORIZONTAL;
			gbc_dichargeCombo.insets = new Insets(0, 0, 5, 5);
			gbc_dichargeCombo.gridx = 1;
			gbc_dichargeCombo.gridy = 2;
			panelHeader.add(getJComboDischarge(), gbc_dichargeCombo);
			GridBagConstraints gbc_destinationLabel = new GridBagConstraints();
			gbc_destinationLabel.anchor = GridBagConstraints.EAST;
			gbc_destinationLabel.insets = new Insets(0, 0, 5, 5);
			gbc_destinationLabel.gridx = 2;
			gbc_destinationLabel.gridy = 2;
			panelHeader.add(getDestinationLabel(), gbc_destinationLabel);
			GridBagConstraints gbc_destinationCombo = new GridBagConstraints();
			gbc_destinationCombo.fill = GridBagConstraints.HORIZONTAL;
			gbc_destinationCombo.insets = new Insets(0, 0, 5, 5);
			gbc_destinationCombo.gridx = 3;
			gbc_destinationCombo.gridy = 2;
			panelHeader.add(getJComboDestination(), gbc_destinationCombo);
			GridBagConstraints gbc_specificRadio = new GridBagConstraints();
			gbc_specificRadio.anchor = GridBagConstraints.EAST;
			gbc_specificRadio.insets = new Insets(0, 0, 0, 5);
			gbc_specificRadio.gridx = 0;
			gbc_specificRadio.gridy = 3;
			panelHeader.add(getSpecificRadio(), gbc_specificRadio);
			GridBagConstraints gbc_codeTextField = new GridBagConstraints();
			gbc_codeTextField.insets = new Insets(0, 0, 0, 5);
			gbc_codeTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_codeTextField.gridx = 1;
			gbc_codeTextField.gridy = 3;
			panelHeader.add(getCodeTextField(), gbc_codeTextField);
			GridBagConstraints gbc_selectButton = new GridBagConstraints();
			gbc_selectButton.anchor = GridBagConstraints.CENTER;
			gbc_selectButton.insets = new Insets(0, 0, 0, 5);
			gbc_selectButton.gridx = 3;
			gbc_selectButton.gridy = 3;
			panelHeader.add(getSelectedButton(), gbc_selectButton);
			
		}
		return panelHeader;
	}

	private JPanel getPanelContent() {
		if (panelContent == null) {
			panelContent = new JPanel();
			GridBagLayout gbl_panelContent = new GridBagLayout();
			gbl_panelContent.columnWidths = new int[] { 452, 0 };
			gbl_panelContent.rowHeights = new int[] { 402, 0 };
			gbl_panelContent.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_panelContent.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
			panelContent.setLayout(gbl_panelContent);
			GridBagConstraints gbc_scrollPaneInventory = new GridBagConstraints();
			gbc_scrollPaneInventory.fill = GridBagConstraints.BOTH;
			gbc_scrollPaneInventory.gridx = 0;
			gbc_scrollPaneInventory.gridy = 0;
			panelContent.add(getScrollPaneInventory(), gbc_scrollPaneInventory);
		}
		return panelContent;
	}

	private JPanel getPanelFooter() {
		if (panelFooter == null) {
			panelFooter = new JPanel();
			panelFooter.add(getSaveButton());
			panelFooter.add(getDeleteButton());
			panelFooter.add(getLotButton());
			panelFooter.add(getCleanTableButton());
			panelFooter.add(getCloseButton());
		}
		return panelFooter;
	}

	private GoodDateChooser getJCalendarFrom() {
		if (jCalendarInventory == null) {
			jCalendarInventory = new GoodDateChooser(LocalDate.now(), false, false);
			if (inventory != null) {
				jCalendarInventory.setDate(inventory.getInventoryDate().toLocalDate());
				dateInventory = inventory.getInventoryDate();
			}
			jCalendarInventory.addDateChangeListener(event -> {
				dateInventory = jCalendarInventory.getDate().atStartOfDay();
			});
		}
		return jCalendarInventory;
	}

	private JButton getSaveButton() {
		saveButton = new JButton(MessageBundle.getMessage("angal.common.save.btn"));
		saveButton.setMnemonic(MessageBundle.getMnemonic("angal.common.save.btn.key"));
		saveButton.addActionListener(actionEvent -> {
			String state = InventoryStatus.draft.toString();
			String user = UserBrowsingManager.getCurrentUser();
			if (inventoryRowSearchList == null || inventoryRowSearchList.isEmpty()) {
				MessageDialog.error(null, "angal.inventory.cannotsaveinventorywithoutproducts.msg");
				return;
			}
			try {
				if (!lotsDeleted.isEmpty() || !inventoryRowsToDelete.isEmpty()) {
					for (Map.Entry<Integer, Lot> entry : lotsDeleted.entrySet()) {
						MedicalInventoryRow invRow = null;
						invRow = medicalInventoryRowManager.getMedicalInventoryRowById(entry.getKey());
						if (invRow != null ) {
							invRow.setLot(null);
							medicalInventoryRowManager.updateMedicalInventoryRow(invRow);
							movStockInsertingManager.deleteLot(entry.getValue());
						}
						
			        }
					medicalInventoryRowManager.deleteMedicalInventoryRows(inventoryRowsToDelete);
				}
				if (mode.equals("new")) {
					newReference = referenceTextField.getText().trim();
					boolean refExist = false;
					refExist =  medicalInventoryManager.referenceExists(newReference);
					if (refExist) {
						MessageDialog.error(null, "angal.inventory.referencealreadyused.msg");
						return;
					}
					inventory = new MedicalInventory();
					inventory.setInventoryReference(newReference);
					inventory.setInventoryDate(dateInventory);
					inventory.setStatus(state);
					inventory.setUser(user);
					inventory.setInventoryType(InventoryType.main.toString());
					if (chargeType != null) {
						inventory.setChargeType(chargeType.getCode());
					} else {
						inventory.setChargeType(null);
					}
					if (dischargeType != null) {
						inventory.setDischargeType(dischargeType.getCode());
					} else {
						inventory.setDischargeType(null);
					}
					if (supplier != null) {
						inventory.setSupplier(supplier.getSupId());
					} else {
						inventory.setSupplier(null);
					}
					if (destination != null) {
						inventory.setDestination(destination.getCode());
					} else {
						inventory.setDestination(null);
					}
					inventory = medicalInventoryManager.newMedicalInventory(inventory);
					for (Iterator<MedicalInventoryRow> iterator = inventoryRowSearchList.iterator(); iterator.hasNext();) {
						MedicalInventoryRow medicalInventoryRow = (MedicalInventoryRow) iterator.next();
						medicalInventoryRow.setInventory(inventory);
						Lot lot = medicalInventoryRow.getLot();
						String lotCode;
						Medical medical = medicalInventoryRow.getMedical();
						if (lot != null) {
							lotCode = lot.getCode();
							Lot lotExist = null;
							lotExist = movStockInsertingManager.getLot(lotCode);
							if (lotExist != null) {
								Lot lotStore = null;
								lotStore = movStockInsertingManager.updateLot(lot);
								medicalInventoryRow.setLot(lotStore);
							} else {
								if (lot.getDueDate() != null) {
									Lot lotStore = null;
									lotStore = movStockInsertingManager.storeLot(lotCode, lot, medical);
									medicalInventoryRow.setLot(lotStore);
									medicalInventoryRow.setNewLot(true);
								} else {
									medicalInventoryRow.setLot(null);
								}
							}
						} else {
							medicalInventoryRow.setLot(null);
						}
						medicalInventoryRowManager.newMedicalInventoryRow(medicalInventoryRow);
					}
					// enable validation
					mode = "update";
					MessageDialog.info(this, "angal.inventory.savesuccess.msg");
					fireInventoryInserted();
					resetVariable();
					int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttocontinueediting.msg");
					if (info != JOptionPane.YES_OPTION) {
						dispose(); 
					}
				} else if (mode.equals("update")) {
					String lastCharge = inventory.getChargeType();
					String lastDischarge = inventory.getDischargeType();
					Integer lastSupplier = inventory.getSupplier();
					String lastDestination = inventory.getDestination();
					String lastReference = inventory.getInventoryReference();
					newReference = referenceTextField.getText().trim();
					MedicalInventory existingInventory =  medicalInventoryManager.getInventoryByReference(newReference);
					if (existingInventory != null && existingInventory.getId() != inventory.getId()) {
						MessageDialog.error(null, "angal.inventory.referencealreadyused.msg");
						return;
					}
					if (inventoryRowListAdded.isEmpty() && lotsSaved.isEmpty() && lotsDeleted.isEmpty()) {
						if ((destination != null && !destination.getCode().equals(lastDestination)) || (chargeType != null && !chargeType.getCode().equals(lastCharge)) || (dischargeType != null && !dischargeType.getCode().equals(lastDischarge)) || (supplier != null && !supplier.getSupId().equals(lastSupplier)) || (destination == null && lastDestination != null) || (chargeType == null && lastCharge != null) || (dischargeType == null && lastDischarge != null) || (supplier == null && lastSupplier != null) || !lastReference.equals(newReference)) {
							if (!inventory.getInventoryDate().equals(dateInventory)) {
								inventory.setInventoryDate(dateInventory);
							}
							if (!inventory.getUser().equals(user)) {
								inventory.setUser(user);
							}
							if (!lastReference.equals(newReference)) {
								inventory.setInventoryReference(newReference);
							}
							MovementType charge = (MovementType) chargeCombo.getSelectedItem();
							if (charge != null) {
								inventory.setChargeType(charge.getCode());
							} else {
								inventory.setChargeType(null);
							}
							MovementType discharge = (MovementType) dischargeCombo.getSelectedItem();
							if (discharge != null) {
								inventory.setDischargeType(discharge.getCode());
							} else {
								inventory.setDischargeType(null);
							}
							Supplier supplier = (Supplier) supplierCombo.getSelectedItem();
							if (supplier != null) {
								inventory.setSupplier(supplier.getSupId());
							} else {
								inventory.setSupplier(null);
							}
							Ward destination = (Ward) destinationCombo.getSelectedItem();
							if (destination != null) {
								inventory.setDestination(destination.getCode());
							} else {
								inventory.setDestination(null);
							}
							inventory = medicalInventoryManager.updateMedicalInventory(inventory);
							if (inventory != null) {
								MessageDialog.info(null, "angal.inventory.update.success.msg");
								resetVariable();
								fireInventoryUpdated();
								int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttocontinueediting.msg");
								if (info != JOptionPane.YES_OPTION) {
									dispose(); 
								}
							} else {
								MessageDialog.error(null, "angal.inventory.update.error.msg");
								return;
							}
						} else {
							if (!inventoryRowsToDelete.isEmpty()) {
								MessageDialog.info(null, "angal.inventory.update.success.msg");
								resetVariable();
								fireInventoryUpdated();
								int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttocontinueediting.msg");
								if (info != JOptionPane.YES_OPTION) {
									dispose(); 
								}
							} else {
								MessageDialog.info(null, "angal.inventory.inventoryisalreadysaved.msg");
								return;
							}
							
						}
						return;
					}
					if (!inventory.getInventoryDate().equals(dateInventory)) {
						inventory.setInventoryDate(dateInventory);
					}
					if (!inventory.getUser().equals(user)) {
						inventory.setUser(user);
					}
					if (!lastReference.equals(newReference)) {
						inventory.setInventoryReference(newReference);
					}
					MovementType charge = (MovementType) chargeCombo.getSelectedItem();
					if (charge != null) {
						inventory.setChargeType(charge.getCode());
					} else {
						inventory.setChargeType(null);
					}
					MovementType discharge = (MovementType) dischargeCombo.getSelectedItem();
					if (discharge != null) {
						inventory.setDischargeType(discharge.getCode());
					} else {
						inventory.setDischargeType(null);
					}
					Supplier supplier = (Supplier) supplierCombo.getSelectedItem();
					if (supplier != null) {
						inventory.setSupplier(supplier.getSupId());
					} else {
						inventory.setSupplier(null);
					}
					Ward destination = (Ward) destinationCombo.getSelectedItem();
					if (destination != null) {
						inventory.setDestination(destination.getCode());
					} else {
						inventory.setDestination(null);
					}
					inventory = medicalInventoryManager.updateMedicalInventory(inventory);
					for (Iterator<MedicalInventoryRow> iterator = inventoryRowSearchList.iterator(); iterator.hasNext();) {
						MedicalInventoryRow medicalInventoryRow = iterator.next();
						Medical medical = medicalInventoryRow.getMedical();
						Lot lot = medicalInventoryRow.getLot();
						String lotCode;
						medicalInventoryRow.setInventory(inventory);
						int id = medicalInventoryRow.getId();
						if (id == 0) {
							if (lot != null) {
								lotCode = lot.getCode();
								boolean isExist = false;
								Lot lotExist = movStockInsertingManager.getLot(lotCode);
								if (lotExist != null) {
									isExist = true;
								}
								if (!isExist) {
									if (lot.getDueDate() != null) {
										Lot lotStore = movStockInsertingManager.storeLot(lotCode, lot, medical);
										medicalInventoryRow.setLot(lotStore);
										medicalInventoryRow.setNewLot(true);
									} else {
										medicalInventoryRow.setLot(null);
									}
								} else {
									Lot lotStore = movStockInsertingManager.updateLot(lot);
									medicalInventoryRow.setLot(lotStore);
								}
							}
							medicalInventoryRowManager.newMedicalInventoryRow(medicalInventoryRow);
						} else {
							lot = medicalInventoryRow.getLot();
							double reatQty = medicalInventoryRow.getRealQty();
							medicalInventoryRow = medicalInventoryRowManager.getMedicalInventoryRowById(id);
							medicalInventoryRow.setRealqty(reatQty);
							if (lot != null) {
								lotCode = lot.getCode();
								Lot lotExist = movStockInsertingManager.getLot(lotCode);
								if (lotExist != null) {
									Lot lotStore;
									lotExist.setDueDate(lot.getDueDate());
									lotExist.setPreparationDate(lot.getPreparationDate());
									lotExist.setCost(lot.getCost());
									lotStore = movStockInsertingManager.updateLot(lotExist);
									medicalInventoryRow.setLot(lotStore);
								} else {
									if (lot.getDueDate() != null) {
										Lot lotStore = movStockInsertingManager.storeLot(lotCode, lot, medical);
										medicalInventoryRow.setLot(lotStore);
										medicalInventoryRow.setNewLot(true);
									} else {
										medicalInventoryRow.setLot(null);
									}
								}
							} else {
								medicalInventoryRow.setLot(null);
							}
							if (medicalInventoryRow.getId() == 0) {
								medicalInventoryRowManager.newMedicalInventoryRow(medicalInventoryRow);
							} else {
								medicalInventoryRowManager.updateMedicalInventoryRow(medicalInventoryRow);
							}
						}
					}
					MessageDialog.info(null, "angal.inventory.update.success.msg");
					resetVariable();
					fireInventoryUpdated();
					int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttocontinueediting.msg");
					if (info != JOptionPane.YES_OPTION) {
						dispose(); 
					}
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
				return;
			}
		});
		return saveButton;
	}
	private JButton getDeleteButton() {
		deleteButton = new JButton(MessageBundle.getMessage("angal.common.delete.btn"));
		deleteButton.setMnemonic(MessageBundle.getMnemonic("angal.common.delete.btn.key"));
		deleteButton.addActionListener(actionEvent -> {
			int[] selectedRows = jTableInventoryRow.getSelectedRows();
			if (selectedRows.length == 0) {
				MessageDialog.error(this, "angal.inventoryrow.pleaseselectatleastoneinventoryrow.msg");
				return;
			}
			int delete = MessageDialog.yesNo(null, "angal.inventoryrow.doyoureallywanttodeletethisinventoryrow.msg");
			if (delete == JOptionPane.YES_OPTION) {
				if (inventory == null) {
					for (int i = selectedRows.length - 1; i >= 0; i--) {
						MedicalInventoryRow selectedInventoryRow = (MedicalInventoryRow) jTableInventoryRow.getValueAt(selectedRows[i], -1);
						inventoryRowSearchList.remove(selectedInventoryRow);
	                }
				} else {
					for (int i = selectedRows.length - 1; i >= 0; i--) {
						MedicalInventoryRow inventoryRow = (MedicalInventoryRow) jTableInventoryRow.getValueAt(selectedRows[i], -1);
						inventoryRowSearchList.remove(inventoryRow);
						if (inventoryRow.getId() != 0) {
							inventoryRowsToDelete.add(inventoryRow);
						}
					}
				}
				jTableInventoryRow.clearSelection();
			} else {
				return;
			}
			fireInventoryUpdated();
		});
		return deleteButton;
	}
	
	private JButton getLotButton() {
		lotButton = new JButton(MessageBundle.getMessage("angal.inventory.lot.btn"));
		lotButton.setMnemonic(MessageBundle.getMnemonic("angal.inventory.lot.btn.key"));
		lotButton.addActionListener(actionEvent -> {
			int selectedRow = jTableInventoryRow.getSelectedRow();
			if (selectedRow == -1) {
				MessageDialog.error(this, "angal.inventoryrow.pleaseselectoneinventoryrow.msg");
				return;
			}
			MedicalInventoryRow selectedInventoryRow = (MedicalInventoryRow) jTableInventoryRow.getValueAt(selectedRow, -1);
			Lot lotToUpdate = selectedInventoryRow.getLot() != null ? selectedInventoryRow.getLot() : null;
			Lot lot = new Lot();
			try {
				lot = this.getLot(lotToUpdate);
				String lotCode = lotToUpdate != null ? lotToUpdate.getCode():"";
				if (lot != null && !lot.getCode().equals(lotCode)) {
					Lot lotDelete = movStockInsertingManager.getLot(lotCode);
					if (lotDelete != null) {
						lotsDeleted.put(selectedInventoryRow.getId(), lotDelete);
					}	
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
				return;
			} 
			if (lot != null) {
				code = lot.getCode(); 
				if (selectedInventoryRow.getLot() == null) {
					List<MedicalInventoryRow> invRows = inventoryRowSearchList.stream().filter(inv -> inv.getLot() != null && inv.getLot().getCode().equals(code)).collect(Collectors.toList()); 
					if (invRows.size() == 0 || code.equals("")) {
						selectedInventoryRow.setNewLot(true);
						selectedInventoryRow.setLot(lot);
						lotsSaved.add(lot);
					} else {
						MessageDialog.error(this, "angal.inventoryrow.thislotcodealreadyexists.msg");
						lotButton.doClick();
					}
				} else {
					List<MedicalInventoryRow> invRows = inventoryRowSearchList.stream().filter(inv -> inv.getMedical().getCode().equals(selectedInventoryRow.getMedical().getCode())).collect(Collectors.toList());
					invRows = invRows.stream().filter(inv -> inv.getLot() != null && inv.getLot().getCode().equals(code)).collect(Collectors.toList());
					if (invRows.size() == 0 || code.equals("")) {
						selectedInventoryRow.setNewLot(true);
						selectedInventoryRow.setLot(lot);
						lotsSaved.add(lot);
					} else {
						MessageDialog.error(this, "angal.inventoryrow.thislotcodealreadyexists.msg");
						lotButton.doClick();
					}
				}
				inventoryRowSearchList.set(selectedRow, selectedInventoryRow);
				jTableInventoryRow.updateUI();
			}
		});
		lotButton.setEnabled(false);
		return lotButton;
	}
	
	private JButton getCloseButton() {
		closeButton = new JButton(MessageBundle.getMessage("angal.common.close.btn"));
		closeButton.setMnemonic(MessageBundle.getMnemonic("angal.common.close.btn.key"));
		closeButton.addActionListener(actionEvent -> {
			String lastCharge = null;
			String lastDischarge = null;
			Integer lastSupplier = null;
			String lastDestination = null;
			String lastReference = null;
			newReference = referenceTextField.getText().trim();
			LocalDateTime lastDate = dateInventory;
			if (inventory != null) {
				lastCharge = inventory.getChargeType();
				lastDischarge = inventory.getDischargeType();
				lastSupplier = inventory.getSupplier();
				lastDestination = inventory.getDestination();
				lastReference = inventory.getInventoryReference();
				lastDate = inventory.getInventoryDate();
			}
			if (!lotsSaved.isEmpty() || !inventoryRowListAdded.isEmpty() || !lotsDeleted.isEmpty() || !inventoryRowsToDelete.isEmpty() || (destination != null && !destination.getCode().equals(lastDestination)) ||  (chargeType != null && !chargeType.getCode().equals(lastCharge)) ||  (dischargeType != null && !dischargeType.getCode().equals(lastDischarge)) || (supplier != null && !supplier.getSupId().equals(lastSupplier)) ||(destination == null && lastDestination != null) ||  (chargeType == null && lastCharge != null) ||  (dischargeType == null && lastDischarge != null) || (supplier == null && lastSupplier != null) || (lastReference != null && !lastReference.equals(newReference)) || !lastDate.toLocalDate().equals(dateInventory.toLocalDate())) {
				int reset = MessageDialog.yesNoCancel(null, "angal.inventoryrow.doyouwanttosavethechanges.msg");
				if (reset == JOptionPane.YES_OPTION) {
					this.saveButton.doClick();
				}
				if (reset == JOptionPane.NO_OPTION) {
					resetVariable();
					dispose();
				} else {
					resetVariable();
					return;
				}
			} else {
				resetVariable();
				dispose();
			}
		});
		return closeButton;
	}
	
	private JButton getCleanTableButton() {
		resetButton = new JButton(MessageBundle.getMessage("angal.inventory.clean.btn"));
		resetButton.setMnemonic(MessageBundle.getMnemonic("angal.inventory.clean.btn.key"));
		resetButton.addActionListener(actionEvent -> {
			int reset = MessageDialog.yesNo(null, "angal.inventoryrow.doyoureallywanttocleanthistable.msg");
			if (reset == JOptionPane.YES_OPTION) {
				if (inventory != null) {
					for (MedicalInventoryRow invRow : inventoryRowSearchList) {
						if (invRow.getId() != 0) {
							inventoryRowsToDelete.add(invRow);
						}
					}
				}
				selectAll = false;
				specificRadio.setSelected(true);
				codeTextField.setEnabled(true);
				inventoryRowSearchList.clear();
				fireInventoryUpdated();
			}
		});
		return resetButton;
	}

	private JPanel getSelectedButton() {
		JPanel pan = new JPanel();
		if (selectButton == null) {
			JLabel texte = new JLabel(MessageBundle.getMessage("angal.inventory.allproduct.txt"));
			selectButton = new JButton(MessageBundle.getMessage("angal.common.select.btn"));
			selectButton.setMnemonic(MessageBundle.getMnemonic("angal.common.select.btn.key"));
			selectButton.addActionListener(actionEvent -> {
		        mainPanel = new JPanel();
		        mainPanel.setLayout(new BorderLayout(10, 10));
		        
		        JPanel leftPanel = new JPanel();
		        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
		        leftPanel.setLayout(new GridLayout(3,1));
		        leftPanel.add(new JLabel(MessageBundle.getMessage("angal.inventoryrow.medicaltype.txt")));
		        leftPanel.add(getJComboMedicalType());
		        
		        ButtonGroup radioGroup = new ButtonGroup();
		        radioGroup.add(all);
		        radioGroup.add(onlyNonZero);
		        radioGroup.add(withMovement);
		        
		        JPanel rightPanel = new JPanel();
		        rightPanel.setLayout(new GridLayout(3, 1, 5, 5));
		        rightPanel.add(getAllRadioButton());
		        rightPanel.add(getMedicalWithNonZeroQuatityRadioButton());
		        rightPanel.add(getMedicalWithMovementRadioButton());
		        
		        JPanel bottomPanel = new JPanel();
		        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		        bottomPanel.add(getOkButton());
		        bottomPanel.add(getCancelButton());
		        
		        mainPanel.add(leftPanel, BorderLayout.WEST);
		        mainPanel.add(rightPanel, BorderLayout.EAST);
		        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		        
		        frame = new JFrame();
		        frame.add(mainPanel);
		        frame.setSize(350, 200);
		        frame.setTitle(MessageBundle.getMessage("angal.inventoryrow.lotinformation.title"));
		        frame.setLocationRelativeTo(null);
		        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		        frame.setVisible(true);
			});
			pan.add(texte);
			pan.add(selectButton);
		}
		return pan;
	}
	
	private JScrollPane getScrollPaneInventory() {
		if (scrollPaneInventory == null) {
			scrollPaneInventory = new JScrollPane();
			try {
				scrollPaneInventory.setViewportView(getJTableInventoryRow());
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		return scrollPaneInventory;
	}
	
	private JTable getJTableInventoryRow() throws OHServiceException {
		if (jTableInventoryRow == null) {
			jTableInventoryRow = new JTable();
			jTetFieldEditor = new JTextField();
			jTableInventoryRow.setFillsViewportHeight(true);
			jTableInventoryRow.setModel(new InventoryRowModel(false, null));
			for (int i = 0; i < pColumnVisible.length; i++) {
				jTableInventoryRow.getColumnModel().getColumn(i).setCellRenderer(new EnabledTableCellRenderer());
				jTableInventoryRow.getColumnModel().getColumn(i).setPreferredWidth(pColumwidth[i]);
				if (i == 0 || !pColumnVisible[i]) {
					jTableInventoryRow.getColumnModel().getColumn(i).setMinWidth(0);
					jTableInventoryRow.getColumnModel().getColumn(i).setMaxWidth(0);
					jTableInventoryRow.getColumnModel().getColumn(i).setPreferredWidth(0);
				}
			}
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	        jTableInventoryRow.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			jTableInventoryRow.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						jTableInventoryRow.editCellAt(jTableInventoryRow.getSelectedRow(), jTableInventoryRow.getSelectedColumn());
						jTetFieldEditor.selectAll();
						int[] selectedRows = jTableInventoryRow.getSelectedRows();
						if (selectedRows.length == 1) {
							MedicalInventoryRow medInvRow = (MedicalInventoryRow) jTableInventoryRow.getValueAt(selectedRows[0], -1);
							if (medInvRow.getLot() == null || medInvRow.isNewLot()) {
								lotButton.setEnabled(true);
			                } else {
			                	lotButton.setEnabled(false);
			                }
						} else {
							lotButton.setEnabled(false);
						}
					}

				}
			});
			DefaultCellEditor cellEditor = new DefaultCellEditor(jTetFieldEditor);
			jTableInventoryRow.setDefaultEditor(Integer.class, cellEditor);
		}
		return jTableInventoryRow;
	}

	class EnabledTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			return cell;
		}
	}
	class InventoryRowModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;
		
		public InventoryRowModel(boolean add, boolean withNonZeroQty, MedicalType medicalTypeSelected) throws OHServiceException {
			inventoryRowList = loadNewInventoryTable(null, inventory, add, withNonZeroQty, medicalTypeSelected);
			if (!inventoryRowList.isEmpty()) {
				for (MedicalInventoryRow invRow : inventoryRowList) {
					addMedInRowInInventorySearchList(invRow);
				}
				selectAll = true;
				MessageDialog.info(null, "angal.inventory.allmedicaladdedsuccessfully.msg");
			} else {
				MessageDialog.info(null, "angal.inventory.youhavealreadyaddedallproduct.msg");
			}
		}

		public InventoryRowModel(boolean withNonZeroQty,  MedicalType medicalTypeSelected) throws OHServiceException {
			if (inventory != null) {
				inventoryRowList = medicalInventoryRowManager.getMedicalInventoryRowByInventoryId(inventory.getId());
			} else {
				if (selectAll) {
					inventoryRowList = loadNewInventoryTable(null, inventory, false, withNonZeroQty, medicalTypeSelected);
				}
			}
			if (!inventoryRowList.isEmpty()) {
				for (MedicalInventoryRow invRow : inventoryRowList) {
					addMedInRowInInventorySearchList(invRow);
					if (invRow.getId() == 0) {
						inventoryRowListAdded.add(invRow);
					}
				}
			}
				
		}

		public Class< ? > getColumnClass(int c) {
			if (c == 0) {
				return Integer.class;
			} else if (c == 1) {
				return String.class;
			} else if (c == 2) {
				return String.class;
			} else if (c == 3) {
				return String.class;
			} else if (c == 4) {
				return String.class;
			} else if (c == 5) {
				return String.class;
			} else if (c == 6) {
				return Integer.class;
			} else if (c == 7) {
				return Integer.class;
			} else if (c == 8) {
				return Integer.class;
			} else if (c == 9) {
				return Integer.class;
			}
			return null;
		}

		public int getRowCount() {
			if (inventoryRowSearchList == null) {
				return 0;
			}
			return inventoryRowSearchList.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			MedicalInventoryRow medInvtRow = inventoryRowSearchList.get(r);
			if (c == -1) {
				return medInvtRow;
			} else if (c == 0) {
				return medInvtRow.getId();
			} else if (c == 1) {
				return medInvtRow.getMedical() == null ? "" : medInvtRow.getMedical().getProdCode();
			} else if (c == 2) {
				return medInvtRow.getMedical() == null ? "" : medInvtRow.getMedical().getDescription();
			} else if (c == 3) {
				if (medInvtRow.getLot() == null || medInvtRow.isNewLot()) {
					return "N";
				}
				return "";
			} else if (c == 4) {
				if (medInvtRow.getLot() == null) {
					return "";
				}
				return medInvtRow.getLot().getCode().equals("") ? "AUTO" : medInvtRow.getLot().getCode();
			} else if (c == 5) {
				if (medInvtRow.getLot() != null) {
					if (medInvtRow.getLot().getDueDate() != null) {
						return medInvtRow.getLot().getDueDate().format(DATE_TIME_FORMATTER);
					}
				}
				return "";
			} else if (c == 6) {
				Double dblVal = medInvtRow.getTheoreticQty();
				return dblVal.intValue();
			} else if (c == 7) {
				Double dblValue = medInvtRow.getRealQty();
				return dblValue.intValue();
			} else if (c == 8) {
				if (medInvtRow.getLot() != null) {
					if (medInvtRow.getLot().getCost() != null) {
						medInvtRow.setTotal(medInvtRow.getRealQty() * medInvtRow.getLot().getCost().doubleValue());
						return medInvtRow.getLot().getCost();
					}
				}
				return 0;
			} else if (c == 9) {
				if (medInvtRow.getLot() != null) {
					if (medInvtRow.getLot().getCost() != null) {
						return medInvtRow.getTotal();
					}
				}
				return 0;
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int r, int c) {
			if (r < inventoryRowSearchList.size()) {
				MedicalInventoryRow invRow = inventoryRowSearchList.get(r);
				if (c == 7) {
					Integer intValue = 0;
					if (value != null) {
						try {
							intValue = Integer.parseInt(value.toString());
						} catch (NumberFormatException e) {
							intValue = 0;
							return;
						}
					}
					if (intValue < 0) {
						MessageDialog.error(null,  "angal.inventoryrow.invalidquantity.msg");
						return;
					}
					invRow.setRealqty(intValue);
					if (invRow.getLot() != null && invRow.getLot().getCost() != null) {
						double total = invRow.getRealQty() * invRow.getLot().getCost().doubleValue();
						invRow.setTotal(total);
					}
					inventoryRowListAdded.add(invRow);
					inventoryRowSearchList.set(r, invRow);
				}
			}

		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnEditable[columnIndex];
		}
	}
	
	private void ajustWith() {
		for (int i = 0; i < jTableInventoryRow.getColumnModel().getColumnCount(); i++) {
			jTableInventoryRow.getColumnModel().getColumn(i).setPreferredWidth(pColumwidth[i]);
			if (i == 0 || !pColumnVisible[i]) {
				jTableInventoryRow.getColumnModel().getColumn(i).setMinWidth(0);
				jTableInventoryRow.getColumnModel().getColumn(i).setMaxWidth(0);
				jTableInventoryRow.getColumnModel().getColumn(i).setPreferredWidth(0);
			}
		}
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        jTableInventoryRow.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	}
	private Lot getLot(Lot lotToUpdate) throws OHServiceException {
		Lot lot = null;
		if (isAutomaticLotIn()) {
			LocalDateTime preparationDate = TimeTools.getNow().truncatedTo(ChronoUnit.MINUTES);
			LocalDateTime expiringDate = askExpiringDate();
			lot = new Lot("", preparationDate, expiringDate);
			// Cost
			BigDecimal cost = new BigDecimal(0);
			if (GeneralData.LOTWITHCOST) {
				cost = askCost(2, cost);
				if (cost.compareTo(new BigDecimal(0)) == 0) {
					return null;
				}
			}
			lot.setCost(cost);
		} else {
			lot = askLot(lotToUpdate);
		}
		return lot;
	}
	
	private Lot askLot(Lot lotToUpdate) {
		LocalDateTime preparationDate;
		LocalDateTime expiringDate;
		Lot lot = null;

		JTextField lotNameTextField = new JTextField(15);
		lotNameTextField.addAncestorListener(new RequestFocusListener());
		TextPrompt suggestion = new TextPrompt(MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotid"), lotNameTextField);
		suggestion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		suggestion.setForeground(Color.GRAY);
		suggestion.setHorizontalAlignment(SwingConstants.CENTER);
		suggestion.changeAlpha(0.5f);
		suggestion.changeStyle(Font.BOLD + Font.ITALIC);
		LocalDate now = LocalDate.now();
		GoodDateChooser preparationDateChooser = new GoodDateChooser(now);
		GoodDateChooser expireDateChooser = new GoodDateChooser(now);
		if (lotToUpdate != null) {
			lotNameTextField.setText(lotToUpdate.getCode());
			preparationDateChooser = new GoodDateChooser(lotToUpdate.getPreparationDate().toLocalDate());
			expireDateChooser = new GoodDateChooser(lotToUpdate.getDueDate().toLocalDate());
		}
		JPanel panel = new JPanel(new GridLayout(3, 2));
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotnumberabb")));
		panel.add(lotNameTextField);
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.preparationdate")));
		panel.add(preparationDateChooser);
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate")));
		panel.add(expireDateChooser);
		do {
			int ok = JOptionPane.showConfirmDialog(
							this,
							panel,
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotinformations"),
							JOptionPane.OK_CANCEL_OPTION);

			if (ok == JOptionPane.OK_OPTION) {
				String lotName = lotNameTextField.getText();
				if (expireDateChooser.getDate().isBefore(preparationDateChooser.getDate())) {
					MessageDialog.error(this, "angal.medicalstock.multiplecharging.expirydatebeforepreparationdate");
				} else {
					expiringDate = expireDateChooser.getDateEndOfDay();
					preparationDate = preparationDateChooser.getDateStartOfDay();
					lot = new Lot(lotName, preparationDate, expiringDate);
					BigDecimal cost = new BigDecimal(0);
					if (GeneralData.LOTWITHCOST) {
						if (lotToUpdate != null) {
							cost = askCost(2, lotToUpdate.getCost());
						} else {
							cost = askCost(2, cost);
						}
						
						if (cost.compareTo(new BigDecimal(0)) == 0) {
							return null;
						} else {
							lot.setCost(cost);
						}
					}
				}
			} else {
				return null;
			}
		} while (lot == null);
		return lot;
	}
	private BigDecimal askCost(int qty, BigDecimal lastCost) {
		double cost = 0.;
		do {
			String input = JOptionPane.showInputDialog(this,
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.unitcost"),
							lastCost);
			if (input != null) {
				try {
					cost = Double.parseDouble(input);
					if (cost < 0) {
						throw new NumberFormatException();
					} else if (cost == 0.) {
						double total = askTotalCost();
						cost = total / qty;
					}
				} catch (NumberFormatException nfe) {
					MessageDialog.error(this, "angal.medicalstock.multiplecharging.pleaseinsertavalidvalue");
				}
			} else {
				return BigDecimal.valueOf(cost);
			}
		} while (cost == 0.);
		return BigDecimal.valueOf(cost);
	}
	
	protected LocalDateTime askExpiringDate() {
		LocalDateTime date = TimeTools.getNow();
		GoodDateTimeSpinnerChooser expireDateChooser = new GoodDateTimeSpinnerChooser(date);
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate")));
		panel.add(expireDateChooser);

		int ok = JOptionPane.showConfirmDialog(this, panel,
						MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate"),
						JOptionPane.OK_CANCEL_OPTION);

		if (ok == JOptionPane.OK_OPTION) {
			date = expireDateChooser.getLocalDateTime();
		}
		return date;
	}
	
	protected double askTotalCost() {
		String input = JOptionPane.showInputDialog(this,
						MessageBundle.getMessage("angal.medicalstock.multiplecharging.totalcost"),
						0.);
		double total = 0.;
		if (input != null) {
			try {
				total = Double.parseDouble(input);
				if (total < 0) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException nfe) {
				MessageDialog.error(this, "angal.medicalstock.multiplecharging.pleaseinsertavalidvalue");
			}
		}
		return total;
	}

	public MedicalInventory getInventory() {
		return inventory;
	}

	public void setInventory(MedicalInventory inventory) {
		this.inventory = inventory;
	}

	private JRadioButton getSpecificRadio() {
		if (specificRadio == null) {
			specificRadio = new JRadioButton(MessageBundle.getMessage("angal.inventory.specificproduct.txt"));
			specificRadio.addActionListener(actionEvent -> {
				if (specificRadio.isSelected()) {
					codeTextField.setEnabled(true);
					codeTextField.setText("");
					allRadio.setSelected(false);
				}
			});
		}
		return specificRadio;
	}

	private JRadioButton getAllRadio() {
		if (allRadio == null) {
			allRadio = new JRadioButton(MessageBundle.getMessage("angal.inventory.allproduct.txt"));
			if (inventory != null) {
				allRadio.setSelected(true);
				specificRadio.setSelected(false);
			} else {
				allRadio.setSelected(false);
				specificRadio.setSelected(true);
			}
			allRadio.addActionListener(actionEvent -> {
				if (!selectAll) {
					if (allRadio.isSelected()) {
						codeTextField.setEnabled(false);
						codeTextField.setText("");
						if (inventoryRowSearchList.size() > 0) {
							int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttoaddallnotyetlistedproducts.msg");
							if (info == JOptionPane.YES_OPTION) {
								try {
									allRadio.setSelected(true);
									jTableInventoryRow.setModel(new InventoryRowModel(true, false, null));
								} catch (OHServiceException e) {
									OHServiceExceptionUtil.showMessages(e);
								}
							} else {
								allRadio.setSelected(false);
								specificRadio.setSelected(true);
								selectAll = false;
							}
							
						} else {
							if (mode.equals("update")) {
								try {
									allRadio.setSelected(true);
									jTableInventoryRow.setModel(new InventoryRowModel(true, false, null));
								} catch (OHServiceException e) {
									OHServiceExceptionUtil.showMessages(e);
								}
							} else {
								try {
									jTableInventoryRow.setModel(new InventoryRowModel(false, null));
								} catch (OHServiceException e) {
									OHServiceExceptionUtil.showMessages(e);
								}	
							}
						}
						fireInventoryUpdated();
						code = null;
						ajustWith();
					}
				} else {
					MessageDialog.info(null, "angal.inventory.youhavealreadyaddedallproduct.msg");
				}
			});
		}
		return allRadio;
	}

	private JLabel getDateInventoryLabel() {
		if (dateInventoryLabel == null) {
			dateInventoryLabel = new JLabel(MessageBundle.getMessage("angal.common.date.txt"));
		}
		return dateInventoryLabel;
	}
	
	private JTextField getCodeTextField() {
		if (codeTextField == null) {
			codeTextField = new JTextField();
			if (inventory != null) {
				codeTextField.setEnabled(false);
			} else {
				codeTextField.setEnabled(true);
			}
			codeTextField.setColumns(10);
			TextPrompt suggestion = new TextPrompt(MessageBundle.getMessage("angal.common.code.txt"), codeTextField, Show.FOCUS_LOST);
			suggestion.setFont(new Font("Tahoma", Font.PLAIN, 12));
			suggestion.setForeground(Color.GRAY);
			suggestion.setHorizontalAlignment(JLabel.CENTER);
			suggestion.changeAlpha(0.5f);
			suggestion.changeStyle(Font.BOLD + Font.ITALIC);
			codeTextField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						code = codeTextField.getText().trim();
						code = code.toLowerCase();
						try {
							addInventoryRow(code);
						} catch (OHServiceException e1) {
							OHServiceExceptionUtil.showMessages(e1);
						}
						codeTextField.setText("");
					}
				}
			});
		}
		return codeTextField;
	}

	private List<MedicalInventoryRow> loadNewInventoryTable(String code, MedicalInventory inventory, boolean add, boolean withNonZeroQty, MedicalType medicalTypeSelected) throws OHServiceException {	
		List<MedicalInventoryRow> inventoryRowsList = new ArrayList<>();
		if (inventory != null) {
			int id = inventory.getId();
			inventoryRowsList = medicalInventoryRowManager.getMedicalInventoryRowByInventoryId(id);
			if (add) {
				inventoryRowsList = getMedicalInventoryRows(code);
			}
		} else {
			inventoryRowsList = getMedicalInventoryRows(code);
		}
		if (withNonZeroQty) {
			inventoryRowsList = inventoryRowsList.stream().filter(inv -> inv.getTheoreticQty() > 0).collect(Collectors.toList());
		}
		if (medicalTypeSelected != null) {
			inventoryRowsList = inventoryRowsList.stream().filter(inv -> inv.getMedical().getType().getDescription().equals(medicalTypeSelected.getDescription())).collect(Collectors.toList());
		}
		return inventoryRowsList;
	}
	
	private List<MedicalInventoryRow> getMedicalInventoryRows(String code) throws OHServiceException {
		List<MedicalInventoryRow> inventoryRowsList = new ArrayList<>();
		List<Medical> medicalList = new ArrayList<>();
		List<Lot> lots = null;
		Medical medical = null;
		MedicalInventoryRow inventoryRowTemp = null;
		if (code != null) {
			medical = medicalBrowsingManager.getMedicalByMedicalCode(code);
			if (medical != null) {
				medicalList.add(medical);
			} else {
				medical = chooseMedical(code);
				if (medical != null) {
					medicalList.add(medical);
				}
			}
		} else {
			medicalList = medicalBrowsingManager.getMedicals();
		}
		for (Iterator<Medical> iterator = medicalList.iterator(); iterator.hasNext();) {
			Medical med = (Medical) iterator.next();
			lots = movStockInsertingManager.getLotByMedical(med, false);
			double actualQty = med.getInitialqty() + med.getInqty() - med.getOutqty();
			if (lots.size() == 0) {
				inventoryRowTemp = new MedicalInventoryRow(0, actualQty, actualQty, null, med, null);
				if (!existInInventorySearchList(inventoryRowTemp)) {
					inventoryRowsList.add(inventoryRowTemp);
				}
			} else {
				for (Iterator<Lot> iterator2 = lots.iterator(); iterator2.hasNext();) {
					Lot lot = (Lot) iterator2.next();
					inventoryRowTemp = new MedicalInventoryRow(0, lot.getMainStoreQuantity(), lot.getMainStoreQuantity(), null, med, lot);
					if (!existInInventorySearchList(inventoryRowTemp)) {
						inventoryRowsList.add(inventoryRowTemp);
					}
				}
			}
		}
		return inventoryRowsList;
	}
	private void addInventoryRow(String code) throws OHServiceException {
		List<MedicalInventoryRow> inventoryRowsList = new ArrayList<MedicalInventoryRow>();
		List<Medical> medicalList = new ArrayList<Medical>();
		List<Lot> lots = null;
		Medical medical = null;
		MedicalInventoryRow inventoryRowTemp = null;
		if (code != null) {
			medical = medicalBrowsingManager.getMedicalByMedicalCode(code);
			if (medical != null) {
				medicalList.add(medical);
			} else {
				medical = chooseMedical(code);
				if (medical != null) {
					medicalList.add(medical);
				}
			}
		} else {
			medicalList = medicalBrowsingManager.getMedicals();
		}
		int numberOfMedicalWithoutSameLotAdded = 0;
		Medical medicalWithLot  = null;
		for (Iterator<Medical> iterator = medicalList.iterator(); iterator.hasNext();) {
			Medical med = (Medical) iterator.next();
			lots = movStockInsertingManager.getLotByMedical(med, false);
			if (lots.size() == 0) {
				inventoryRowTemp = new MedicalInventoryRow(0, 0.0, 0.0, null, med, null);
				if (!existInInventorySearchList(inventoryRowTemp)) {
					inventoryRowsList.add(inventoryRowTemp);
				} else {
					int info = MessageDialog.yesNo(null, "angal.inventory.productalreadyexist.msg", med.getDescription());
					if (info == JOptionPane.YES_OPTION) {
						inventoryRowsList.add(inventoryRowTemp);
					}
				}
			} else {
				medicalWithLot = med;
				for (Iterator<Lot> iterator2 = lots.iterator(); iterator2.hasNext();) {
					Lot lot = (Lot) iterator2.next();
					inventoryRowTemp = new MedicalInventoryRow(0, lot.getMainStoreQuantity(), lot.getMainStoreQuantity(), null, med, lot);
					if (!existInInventorySearchList(inventoryRowTemp)) {
						inventoryRowsList.add(inventoryRowTemp);
						numberOfMedicalWithoutSameLotAdded = numberOfMedicalWithoutSameLotAdded + 1;
					}
				}
			}
		}
		if (medicalWithLot != null && numberOfMedicalWithoutSameLotAdded == 0) {
			int info = MessageDialog.yesNo(null, "angal.inventory.productalreadyexist.msg", medicalWithLot.getDescription());
			if (info == JOptionPane.YES_OPTION) {
				inventoryRowTemp = new MedicalInventoryRow(0, 0.0, 0.0, null, medicalWithLot, null);
				inventoryRowsList.add(inventoryRowTemp);
			}
		}
		for (MedicalInventoryRow inventoryRow : inventoryRowsList) {
			addMedInRowInInventorySearchList(inventoryRow);
		}
		jTableInventoryRow.updateUI();
	}

	private Medical chooseMedical(String text) throws OHServiceException {
		Map<String, Medical> medicalMap;
		List<Medical> medicals = medicalBrowsingManager.getMedicals();
		medicalMap = new HashMap<String, Medical>();
		for (Medical med : medicals) {
			String key = med.getProdCode().toLowerCase();
			key = med.getCode().toString().toLowerCase();
			medicalMap.put(key, med);
		}
		ArrayList<Medical> medList = new ArrayList<Medical>();
		for (Medical aMed : medicalMap.values()) {
			if (NormalizeString.normalizeContains(aMed.getDescription().toLowerCase(), text)) {
				medList.add(aMed);
			}
		}
		Collections.sort(medList);
		Medical med = null;
		if (!medList.isEmpty()) {
			MedicalPicker framas = new MedicalPicker(new StockMedModel(medList), medList);
			framas.setSize(300, 400);
			JDialog dialog = new JDialog();
			dialog.setLocationRelativeTo(null);
			dialog.setSize(600, 350);
			dialog.setLocationRelativeTo(null);
			dialog.setModal(true);
			dialog.setTitle(MessageBundle.getMessage("angal.medicalstock.multiplecharging.selectmedical.title"));
			framas.setParentFrame(dialog);
			dialog.setContentPane(framas);
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			med = framas.getSelectedMedical();
			return med;
		}
		return null;
	}

	public EventListenerList getInventoryListeners() {
		return InventoryListeners;
	}

	public void setInventoryListeners(EventListenerList inventoryListeners) {
		InventoryListeners = inventoryListeners;
	}

	private JLabel getReferenceLabel() {
		if (referenceLabel == null) {
			referenceLabel = new JLabel(MessageBundle.getMessage("angal.common.reference.label"));
		}
		return referenceLabel;
	}

	private JLabel getChargeLabel() {
		if (chargeTypeLabel == null) {
			chargeTypeLabel = new JLabel(MessageBundle.getMessage("angal.inventory.chargetype.label"));
		}
		return chargeTypeLabel;
	}
	
	private JLabel getSupplierLabel() {
		if (supplierLabel == null) {
			supplierLabel = new JLabel(MessageBundle.getMessage("angal.inventory.supplier.label"));
		}
		return supplierLabel;
	}
	
	private JLabel getDischargeLabel() {
		if (dischargeTypeLabel == null) {
			dischargeTypeLabel = new JLabel(MessageBundle.getMessage("angal.inventory.dischargetype.label"));
		}
		return dischargeTypeLabel;
	}
	
	private JLabel getDestinationLabel() {
		if (destinationLabel == null) {
			destinationLabel = new JLabel(MessageBundle.getMessage("angal.inventory.destination.label"));
		}
		return destinationLabel;
	}
	
	private JComboBox<MovementType> getJComboCharge() {
		MovementType movementSelected = null;
		if (chargeCombo == null) {
			chargeCombo = new JComboBox<MovementType>();
			try {
				List<MovementType> movementTypes = movTypeManager.getMedicalDsrStockMovementType();	
				chargeCombo.addItem(null);
				for (MovementType movType: movementTypes) {
					if (movType.getType().equals("+")) {
						chargeCombo.addItem(movType);
						if (inventory != null && movType.getCode().equals(inventory.getChargeType())) {
							movementSelected = movType;
						}
					}
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
			if (inventory != null) {
				chargeCombo.setSelectedItem(movementSelected);
				chargeType = movementSelected;
			}
			chargeCombo.addActionListener(actionEvent -> {
				chargeType = (MovementType) chargeCombo.getSelectedItem();
			});
		}
		return chargeCombo;
	}
	
	private JComboBox<MovementType> getJComboDischarge() {
		MovementType movementSelected = null;
		if (dischargeCombo == null) {
			dischargeCombo = new JComboBox<MovementType>();
			try {
				List<MovementType> movementTypes = movTypeManager.getMedicalDsrStockMovementType();
				dischargeCombo.addItem(null);
				for (MovementType movType: movementTypes) {
					if(movType.getType().equals("-")) {
						dischargeCombo.addItem(movType);
						if (inventory != null && movType.getCode().equals(inventory.getDischargeType())) {
							movementSelected = movType;
						}
					}	
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
			if (inventory != null) {
				dischargeCombo.setSelectedItem(movementSelected);
				dischargeType = movementSelected;
			}
			dischargeCombo.addActionListener(actionEvent -> {
				dischargeType = (MovementType) dischargeCombo.getSelectedItem();
			});
		}
		return dischargeCombo;
	}
	
	private JComboBox<Supplier> getJComboSupplier() {
		Supplier supplierSelected = null;
		if (supplierCombo == null) {
			supplierCombo = new JComboBox<Supplier>();
			try {
				List<Supplier> suppliers = supplierManager.getList();
				supplierCombo.addItem(null);
				for (Supplier supplier: suppliers) {
					supplierCombo.addItem(supplier);
					if (inventory != null && supplier.getSupId() == inventory.getSupplier()) {
						supplierSelected = supplier;
					}
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
			if (inventory != null) {
				supplierCombo.setSelectedItem(supplierSelected);
				supplier = supplierSelected;
			}
			supplierCombo.addActionListener(actionEvent -> {
				supplier = (Supplier) supplierCombo.getSelectedItem();
			});
		}
		return supplierCombo;
	}
	
	private JComboBox<Ward> getJComboDestination() {
		Ward destinationSelected = null;
		if (destinationCombo == null) {
			destinationCombo = new JComboBox<Ward>();
			try {
				List<Ward> wards = wardManager.getWards();
				destinationCombo.addItem(null);
				for (Ward ward: wards) {
					destinationCombo.addItem(ward);
					if (inventory != null && ward.getCode().equals(inventory.getDestination()) ) {
						destinationSelected = ward;
					}
				}
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
			if (inventory != null) {
				destinationCombo.setSelectedItem(destinationSelected);
				destination = destinationSelected;
			}
			destinationCombo.addActionListener(actionEvent -> {
				destination = (Ward) destinationCombo.getSelectedItem();
			});
		}
		return destinationCombo;
	}
	
	private JTextField getReferenceTextField() {
		if (referenceTextField == null) {
			referenceTextField = new JTextField();
			referenceTextField.setColumns(10);
			if (inventory != null && !mode.equals("new")) {
				referenceTextField.setText(inventory.getInventoryReference());
			}
		}
		return referenceTextField;
	}
	
	private int getPosition (MedicalInventoryRow inventoryRow) {
		int position = -1;
		int i = 0;
		for (MedicalInventoryRow invR: inventoryRowSearchList) {
			if (invR.getMedical().getCode().equals(inventoryRow.getMedical().getCode())) {
				position = i;
			}
			i = i + 1;
		}
		return position;
	}
	
	private boolean existInInventorySearchList(MedicalInventoryRow inventoryRow) {
		boolean found = false;
		List<MedicalInventoryRow> invRows = inventoryRowSearchList.stream().filter(inv -> inv.getMedical().getCode().equals(inventoryRow.getMedical().getCode())).collect(Collectors.toList());
		if (invRows.size() > 0) {
			for (MedicalInventoryRow invR: invRows) {
				if (inventoryRow.getLot() != null && invR.getLot() != null) {
					if (inventoryRow.getLot().getCode().equals(invR.getLot().getCode())) {
						found = true;
						break;
					}
				} else {
					if (invR.getLot() == null && inventoryRow.getLot() == null) {
						found = true;
						break;
					}
				}
			}
		}
		
		return found;
	}
	private void addMedInRowInInventorySearchList(MedicalInventoryRow inventoryRow) {
		int position = getPosition(inventoryRow);
		if (position == -1) {
			position = inventoryRowSearchList.size();
			inventoryRowSearchList.add(position, inventoryRow);
		} else {
			inventoryRowSearchList.add(position + 1, inventoryRow);
		}
		if (inventoryRow.getId() == 0) {
			inventoryRowListAdded.add(inventoryRow);
		}
	}
	private void resetVariable() {
		inventoryRowsToDelete.clear();
		lotsDeleted.clear();
		inventoryRowListAdded.clear();
		lotsSaved.clear();
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
    
    private JButton getCancelButton() {
    	if (jButtonCancel == null) {
    		jButtonCancel = new JButton(MessageBundle.getMessage("angal.common.cancel.btn"));
    		jButtonCancel.setMnemonic(MessageBundle.getMnemonic("angal.common.cancel.btn.key"));
    	}
		jButtonCancel.addActionListener(actionEvent -> {
			frame.dispose();
		});
		return jButtonCancel;
	}
    
    private JButton getOkButton() {
    	if (jButtonOk == null) {
    		jButtonOk = new JButton(MessageBundle.getMessage("angal.common.ok.btn"));
    		jButtonOk.setMnemonic(MessageBundle.getMnemonic("angal.common.ok.btn.key"));
    	}
    	jButtonOk.addActionListener(actionEvent -> {
    		medicalTypeSelected = (MedicalType) medicalTypeComboBox.getSelectedItem();
    		if (medicalTypeSelected.getDescription().equals(MessageBundle.getMessage("angal.common.all.txt"))) {
    			if (all.isSelected()) {
    				selectAll = true;
    				codeTextField.setEnabled(false);
					codeTextField.setText("");
					if (inventoryRowSearchList.size() > 0) {
						int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttoaddallnotyetlistedproducts.msg");
						if (info == JOptionPane.YES_OPTION) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, false, null));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							specificRadio.setSelected(true);
							selectAll = false;
							frame.dispose();
						}
						
					} else {
						if (mode.equals("update")) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, false, null));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(false, null));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}	
						}
					}
					code = null;
					ajustWith();
					frame.dispose();
    			}
    			if (onlyNonZero.isSelected()) {
    				selectAll = true;
    				if (inventoryRowSearchList.size() > 0) {
						int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttoaddallnotyetlistedproducts.msg");
						if (info == JOptionPane.YES_OPTION) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, true, null));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							specificRadio.setSelected(true);
							selectAll = false;
							frame.dispose();
						}
						
					} else {
						if (mode.equals("update")) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, true, null));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, null));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}	
						}
					}
					code = null;
					ajustWith();
					frame.dispose();
    			}
    			if (withMovement.isSelected()) {
    				
    			}
    		} else {
    			if (all.isSelected()) {
    				selectAll = true;
    				codeTextField.setEnabled(false);
					codeTextField.setText("");
					if (inventoryRowSearchList.size() > 0) {
						int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttoaddallnotyetlistedproducts.msg");
						if (info == JOptionPane.YES_OPTION) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, false, medicalTypeSelected));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							specificRadio.setSelected(true);
							selectAll = false;
							frame.dispose();
						}
						
					} else {
						if (mode.equals("update")) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, false, medicalTypeSelected));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(false, medicalTypeSelected));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}	
						}
					}
					code = null;
					ajustWith();
					frame.dispose();
    			}
    			if (onlyNonZero.isSelected()) {
    				selectAll = true;
    				if (inventoryRowSearchList.size() > 0) {
						int info = MessageDialog.yesNo(null, "angal.inventoryrow.doyouwanttoaddallnotyetlistedproducts.msg");
						if (info == JOptionPane.YES_OPTION) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, true, medicalTypeSelected));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							specificRadio.setSelected(true);
							selectAll = false;
							frame.dispose();
						}
						
					} else {
						if (mode.equals("update")) {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, true, medicalTypeSelected));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}
						} else {
							try {
								jTableInventoryRow.setModel(new InventoryRowModel(true, medicalTypeSelected));
							} catch (OHServiceException e) {
								OHServiceExceptionUtil.showMessages(e);
							}	
						}
					}
					code = null;
					ajustWith();
					frame.dispose();
    			}
    			if (withMovement.isSelected()) {
    				
    			}
    		}
		});
		return jButtonOk;
	}
}
