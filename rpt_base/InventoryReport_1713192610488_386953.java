/*
 * Generated by JasperReports - 4/15/24 3:50 PM
 */
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.*;

import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import java.net.*;

import net.sf.jasperreports.engine.*;
import java.util.*;
import net.sf.jasperreports.engine.data.*;


/**
 *
 */
public class InventoryReport_1713192610488_386953 extends JREvaluator
{


    /**
     *
     */
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_Email = null;
    private JRFillParameter parameter_Address = null;
    private JRFillParameter parameter_Telephone = null;
    private JRFillParameter parameter_REPORT_TIME_ZONE = null;
    private JRFillParameter parameter_REPORT_TEMPLATES = null;
    private JRFillParameter parameter_SUBREPORT_DIR = null;
    private JRFillParameter parameter_REPORT_MAX_COUNT = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_RESOURCE_BUNDLE = null;
    private JRFillParameter parameter_inventoryID = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_Hospital = null;
    private JRFillParameter parameter_IS_IGNORE_PAGINATION = null;
    private JRFillParameter parameter_inventoryReference = null;
    private JRFillParameter parameter_inventoryDate = null;
    private JRFillParameter parameter_REPORT_LOCALE = null;
    private JRFillParameter parameter_inventoryStatus = null;
    private JRFillParameter parameter_City = null;
    private JRFillParameter parameter_REPORT_FILE_RESOLVER = null;
    private JRFillParameter parameter_REPORT_FORMAT_FACTORY = null;
    private JRFillParameter parameter_REPORT_CLASS_LOADER = null;
    private JRFillParameter parameter_REPORT_URL_HANDLER_FACTORY = null;
    private JRFillParameter parameter_REPORT_VIRTUALIZER = null;
    private JRFillParameter parameter_printQtyReal = null;
    private JRFillField field_MINVTR_ID = null;
    private JRFillField field_MINVTR_REAL_QTY = null;
    private JRFillField field_MINVT_ID = null;
    private JRFillField field_MDSR_ID = null;
    private JRFillField field_MDSR_DESC = null;
    private JRFillField field_LT_ID_A = null;
    private JRFillField field_LT_DUE_DATE = null;
    private JRFillField field_LT_COST = null;
    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;
    private JRFillVariable variable_MEDICAL_TOTAL_COST = null;


    /**
     *
     */
    public void customizedInit(
        Map pm,
        Map fm,
        Map vm
        )
    {
        initParams(pm);
        initFields(fm);
        initVars(vm);
    }


    /**
     *
     */
    private void initParams(Map pm)
    {
        parameter_REPORT_CONNECTION = (JRFillParameter)pm.get("REPORT_CONNECTION");
        parameter_Email = (JRFillParameter)pm.get("Email");
        parameter_Address = (JRFillParameter)pm.get("Address");
        parameter_Telephone = (JRFillParameter)pm.get("Telephone");
        parameter_REPORT_TIME_ZONE = (JRFillParameter)pm.get("REPORT_TIME_ZONE");
        parameter_REPORT_TEMPLATES = (JRFillParameter)pm.get("REPORT_TEMPLATES");
        parameter_SUBREPORT_DIR = (JRFillParameter)pm.get("SUBREPORT_DIR");
        parameter_REPORT_MAX_COUNT = (JRFillParameter)pm.get("REPORT_MAX_COUNT");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)pm.get("REPORT_SCRIPTLET");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)pm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_RESOURCE_BUNDLE = (JRFillParameter)pm.get("REPORT_RESOURCE_BUNDLE");
        parameter_inventoryID = (JRFillParameter)pm.get("inventoryID");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)pm.get("REPORT_DATA_SOURCE");
        parameter_Hospital = (JRFillParameter)pm.get("Hospital");
        parameter_IS_IGNORE_PAGINATION = (JRFillParameter)pm.get("IS_IGNORE_PAGINATION");
        parameter_inventoryReference = (JRFillParameter)pm.get("inventoryReference");
        parameter_inventoryDate = (JRFillParameter)pm.get("inventoryDate");
        parameter_REPORT_LOCALE = (JRFillParameter)pm.get("REPORT_LOCALE");
        parameter_inventoryStatus = (JRFillParameter)pm.get("inventoryStatus");
        parameter_City = (JRFillParameter)pm.get("City");
        parameter_REPORT_FILE_RESOLVER = (JRFillParameter)pm.get("REPORT_FILE_RESOLVER");
        parameter_REPORT_FORMAT_FACTORY = (JRFillParameter)pm.get("REPORT_FORMAT_FACTORY");
        parameter_REPORT_CLASS_LOADER = (JRFillParameter)pm.get("REPORT_CLASS_LOADER");
        parameter_REPORT_URL_HANDLER_FACTORY = (JRFillParameter)pm.get("REPORT_URL_HANDLER_FACTORY");
        parameter_REPORT_VIRTUALIZER = (JRFillParameter)pm.get("REPORT_VIRTUALIZER");
        parameter_printQtyReal = (JRFillParameter)pm.get("printQtyReal");
    }


    /**
     *
     */
    private void initFields(Map fm)
    {
        field_MINVTR_ID = (JRFillField)fm.get("MINVTR_ID");
        field_MINVTR_REAL_QTY = (JRFillField)fm.get("MINVTR_REAL_QTY");
        field_MINVT_ID = (JRFillField)fm.get("MINVT_ID");
        field_MDSR_ID = (JRFillField)fm.get("MDSR_ID");
        field_MDSR_DESC = (JRFillField)fm.get("MDSR_DESC");
        field_LT_ID_A = (JRFillField)fm.get("LT_ID_A");
        field_LT_DUE_DATE = (JRFillField)fm.get("LT_DUE_DATE");
        field_LT_COST = (JRFillField)fm.get("LT_COST");
    }


    /**
     *
     */
    private void initVars(Map vm)
    {
        variable_PAGE_NUMBER = (JRFillVariable)vm.get("PAGE_NUMBER");
        variable_COLUMN_NUMBER = (JRFillVariable)vm.get("COLUMN_NUMBER");
        variable_REPORT_COUNT = (JRFillVariable)vm.get("REPORT_COUNT");
        variable_PAGE_COUNT = (JRFillVariable)vm.get("PAGE_COUNT");
        variable_COLUMN_COUNT = (JRFillVariable)vm.get("COLUMN_COUNT");
        variable_MEDICAL_TOTAL_COST = (JRFillVariable)vm.get("MEDICAL_TOTAL_COST");
    }


    /**
     *
     */
    public Object evaluate(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.String)("./rpt/");//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_LT_COST.getValue()));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.Double)(new Double(0.0));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Hospital.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Address.getValue()) + " - " + ((java.lang.String)parameter_City.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Email.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Telephone.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.reportinventory.title" ) + ((java.lang.String)parameter_inventoryID.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryStatus.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryDate.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.common.date")+":");//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.inventory.state")+":");//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.inventory.ref")+":");//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryReference.getValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.report.inventoryreport.inventoriedproducts" ));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.common.code.txt" ));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.product.col" ));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.wardpharmacy.lotnumber.col" ));//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.medicalstock.duedate.col" ));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventoryrow.realqty.col" ));//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventoryrow.unitprice.col" ));//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.totalprice" ));//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.Number)(((java.lang.Integer)field_MDSR_ID.getValue()));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)(((java.lang.String)field_MDSR_DESC.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.String)(((java.lang.String)field_LT_ID_A.getValue())==null?"":((java.lang.String)field_LT_ID_A.getValue()));//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.String)((((java.lang.Integer)parameter_printQtyReal.getValue()).intValue() == 1 ? "" : new DecimalFormat("0").format(((java.lang.Float)field_MINVTR_REAL_QTY.getValue())) + ""));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.String)(((java.lang.String)field_LT_DUE_DATE.getValue())==null?"":((java.lang.String)field_LT_DUE_DATE.getValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_LT_COST.getValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.Double)(new Double(((java.lang.Double)field_LT_COST.getValue()).doubleValue() * ((java.lang.Float)field_MINVTR_REAL_QTY.getValue())));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.list.page" ) +" "+ ((java.lang.Integer)variable_PAGE_NUMBER.getValue()) +" "+ ((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.list.of" ));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)(" " + ((java.lang.Integer)variable_PAGE_NUMBER.getValue()));//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.util.Date)(new Date());//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.report.inventoryreport.summaryprice" ));//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_MEDICAL_TOTAL_COST.getValue()));//$JR_EXPR_ID=41$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateOld(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.String)("./rpt/");//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_LT_COST.getOldValue()));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.Double)(new Double(0.0));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Hospital.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Address.getValue()) + " - " + ((java.lang.String)parameter_City.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Email.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Telephone.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.reportinventory.title" ) + ((java.lang.String)parameter_inventoryID.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryStatus.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryDate.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.common.date")+":");//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.inventory.state")+":");//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.inventory.ref")+":");//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryReference.getValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.report.inventoryreport.inventoriedproducts" ));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.common.code.txt" ));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.product.col" ));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.wardpharmacy.lotnumber.col" ));//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.medicalstock.duedate.col" ));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventoryrow.realqty.col" ));//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventoryrow.unitprice.col" ));//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.totalprice" ));//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.Number)(((java.lang.Integer)field_MDSR_ID.getOldValue()));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)(((java.lang.String)field_MDSR_DESC.getOldValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.String)(((java.lang.String)field_LT_ID_A.getOldValue())==null?"":((java.lang.String)field_LT_ID_A.getOldValue()));//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.String)((((java.lang.Integer)parameter_printQtyReal.getValue()).intValue() == 1 ? "" : new DecimalFormat("0").format(((java.lang.Float)field_MINVTR_REAL_QTY.getOldValue())) + ""));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.String)(((java.lang.String)field_LT_DUE_DATE.getOldValue())==null?"":((java.lang.String)field_LT_DUE_DATE.getOldValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_LT_COST.getOldValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.Double)(new Double(((java.lang.Double)field_LT_COST.getOldValue()).doubleValue() * ((java.lang.Float)field_MINVTR_REAL_QTY.getOldValue())));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.list.page" ) +" "+ ((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()) +" "+ ((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.list.of" ));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)(" " + ((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()));//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.util.Date)(new Date());//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.report.inventoryreport.summaryprice" ));//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_MEDICAL_TOTAL_COST.getOldValue()));//$JR_EXPR_ID=41$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateEstimated(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.String)("./rpt/");//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_LT_COST.getValue()));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.Double)(new Double(0.0));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Hospital.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Address.getValue()) + " - " + ((java.lang.String)parameter_City.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Email.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_Telephone.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.reportinventory.title" ) + ((java.lang.String)parameter_inventoryID.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryStatus.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryDate.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.common.date")+":");//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.inventory.state")+":");//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString("angal.inventory.ref")+":");//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.String)(((java.lang.String)parameter_inventoryReference.getValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.report.inventoryreport.inventoriedproducts" ));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.common.code.txt" ));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.product.col" ));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.wardpharmacy.lotnumber.col" ));//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.medicalstock.duedate.col" ));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventoryrow.realqty.col" ));//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventoryrow.unitprice.col" ));//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.totalprice" ));//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.Number)(((java.lang.Integer)field_MDSR_ID.getValue()));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)(((java.lang.String)field_MDSR_DESC.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.String)(((java.lang.String)field_LT_ID_A.getValue())==null?"":((java.lang.String)field_LT_ID_A.getValue()));//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.String)((((java.lang.Integer)parameter_printQtyReal.getValue()).intValue() == 1 ? "" : new DecimalFormat("0").format(((java.lang.Float)field_MINVTR_REAL_QTY.getValue())) + ""));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.String)(((java.lang.String)field_LT_DUE_DATE.getValue())==null?"":((java.lang.String)field_LT_DUE_DATE.getValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_LT_COST.getValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.Double)(new Double(((java.lang.Double)field_LT_COST.getValue()).doubleValue() * ((java.lang.Float)field_MINVTR_REAL_QTY.getValue())));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.list.page" ) +" "+ ((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()) +" "+ ((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.inventory.list.of" ));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)(" " + ((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()));//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.util.Date)(new Date());//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.lang.String)(((java.util.ResourceBundle)parameter_REPORT_RESOURCE_BUNDLE.getValue()).getString( "angal.report.inventoryreport.summaryprice" ));//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_MEDICAL_TOTAL_COST.getEstimatedValue()));//$JR_EXPR_ID=41$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
