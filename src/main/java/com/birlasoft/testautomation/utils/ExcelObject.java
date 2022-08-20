package com.birlasoft.testautomation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelObject {
	private static Logger LOGGER = Logger.getLogger(ExcelObject.class);
	
	private String excelPath;
	private Workbook workbook;
	private Sheet activeSheet;

	public ExcelObject(String excelPath, String... sheetNames) throws IOException {
		this.excelPath = excelPath;

		File excelFile = new File(excelPath);
		FileInputStream fis = new FileInputStream(excelFile);

		if (excelFile.getName().toLowerCase().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(fis);
		} else {
			workbook = new HSSFWorkbook(fis);
		}

		fis.close();

		if (sheetNames != null && sheetNames.length >= 1) {
			activeSheet = workbook.getSheet(sheetNames[0]);
		} else {
			activeSheet = workbook.getSheetAt(0);
		}
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getActiveSheet() {
		return activeSheet;
	}

	private boolean isSheetExist(String sheetName) {
		return workbook.getSheetIndex(sheetName) != -1;
	}

	public void closeWorkbook() throws IOException {
		this.workbook.close();
	}

	public void createSheets(String... sheetNames) throws IOException {
		File excelFile = new File(this.excelPath);
		FileOutputStream fos = null;

		if (sheetNames != null && sheetNames.length >= 1) {
			for (String sheet : sheetNames) {
				if (!isSheetExist(sheet)) {
					workbook.createSheet(sheet);
				}
			}
		}

		fos = new FileOutputStream(excelFile);
		workbook.write(fos);
		fos.close();
	}

	private int getColumnIndex(Sheet sheet, String columnName) {
		Row row = sheet.getRow(0);
		
		for (Cell cell : row) {
			if (columnName.equals(this.getCellValue(cell))) {
				return cell.getColumnIndex();
			}
		}
		return -1;
	}

	public int getColumnIndex(String sheetName, String columnName) {
		return getColumnIndex(workbook.getSheet(sheetName), columnName);
	}

	public Object getCellValue(Cell cell) {
		Object value = null;
		CellValue cellValue = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell);

		if (cellValue != null) {
			switch (cellValue.getCellType()) {
			case STRING:
				value = cell.getStringCellValue();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					value = cell.getDateCellValue();
				} else {
					value = cell.getNumericCellValue();
				}
				break;
			case BLANK:
			case _NONE:
				value = " ";
				break;
			case BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case ERROR:
			default:
				break;
			}
		} else {
			value = " ";
		}
		return value;
	}

	public Object getCellValue(int rowPosition, int colPosition) {
		Object value = null;
		Cell cell;

		try {
			cell = activeSheet.getRow(rowPosition).getCell(colPosition, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		} catch (NullPointerException e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
			return null;
		}

		value = getCellValue(cell);
		return value;
	}

	public Object getCellValue(Sheet sheet, int rowPosition, int colPosition) {
		Object value = null;
		Cell cell;

		try {
			cell = sheet.getRow(rowPosition).getCell(colPosition, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		} catch (NullPointerException e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
			return null;
		}

		value = getCellValue(cell);
		return value;
	}

	public Object getCellValue(String sheetName, int rowPosition, int colPosition) {
		return getCellValue(workbook.getSheet(sheetName), rowPosition, colPosition);
	}

	public Object getCellValue(String sheetName, int rowPosition, String columnName) {
		return getCellValue(rowPosition, getColumnIndex(workbook.getSheet(sheetName), columnName));
	}

	public Object getCellValue(String sheetName, String columnName, String filterCondition,
			boolean... strictCompareFlag) {
		boolean strictCompare = (strictCompareFlag != null && strictCompareFlag.length >= 1) ? strictCompareFlag[0]
				: false;

		Object value = getCellValue(workbook.getSheet(sheetName),
				getRowIndex(sheetName, filterCondition, strictCompare),
				getColumnIndex(workbook.getSheet(sheetName), columnName));
		return value;
	}

	public int getRowIndex(String sheetName, String filterCondition, boolean strictCompare) {
		String[] conditions = filterCondition.split(Constants.CONDITION_SEPARATOR);
		LinkedHashMap<String, String> fullConditions = new LinkedHashMap<String, String>();

		for (String condition : conditions) {
			fullConditions.put(condition.split(Constants.CONDITIONVALUE_SEPARATOR)[0],
					condition.split(Constants.CONDITIONVALUE_SEPARATOR)[1]);
		}

		int[] columnIndices = new int[fullConditions.size()];
		Set<String> columnNames = fullConditions.keySet();
		Sheet sheet = workbook.getSheet(sheetName);

		for (String columnName : columnNames) {
			columnIndices = ArrayUtils.add(columnIndices, getColumnIndex(sheet, columnName));
			columnIndices = ArrayUtils.remove(columnIndices, 0);
		}

		for (Row row : sheet) {
			LinkedHashMap<String, String> newHashMap = new LinkedHashMap<>();

			for (int index : columnIndices) {
				newHashMap.put(String.valueOf(getCellValue(sheet.getRow(0).getCell(index))),
						String.valueOf(getCellValue(row.getCell(index))));
			}

			if (strictCompare) {
				if (newHashMap.equals(fullConditions))
					return row.getRowNum();
			} else {
				if (compareHashMapLoosely(newHashMap, fullConditions))
					return row.getRowNum();
			}
		}
		return -1;
	}

	private boolean compareHashMapLoosely(LinkedHashMap<String, String> one, LinkedHashMap<String, String> two) {
		Set<String> keySetOne = one.keySet();
		Set<String> keySetTwo = two.keySet();

		if (!CollectionUtils.isEqualCollection(keySetOne, keySetTwo))
			return false;
		for (String col1 : keySetOne) {
			for (String col2 : keySetTwo) {
				if (col1.equalsIgnoreCase(col2)) {
					// Numeric Logic
					if (StringUtils.isNumeric(one.get(col1)) && StringUtils.isNumeric(two.get(col2))) {
						if (Double.valueOf(one.get(col1)).doubleValue() != Double.valueOf(two.get(col2)).doubleValue())
							return false;
					} // Ignore case and trim
					else {
						if (!one.get(col1).trim().equalsIgnoreCase(two.get(col2).trim())) {
							return false;
						}
					}
					// TODO - Logic to be added for dateformat comparisons
				}
			}
		}
		return true;
	}

	public void setCellValue(Sheet sheet, int rowNum, int columnNum, Object valueToSet) throws IOException {
		Cell cell = null;

		try {
			if (sheet.getRow(rowNum) == null)
				cell = sheet.createRow(rowNum).createCell(columnNum);
			else if (sheet.getRow(rowNum).getCell(columnNum) == null) {
				cell = sheet.getRow(rowNum).createCell(columnNum);
			} else {
				cell = sheet.getRow(rowNum).getCell(columnNum);
			}
		} catch (NullPointerException e) {
			LOGGER.error(e.getClass().getSimpleName(), e);
			return;
		}

		if (valueToSet != null) {
			switch (valueToSet.getClass().getSimpleName().toUpperCase()) {
			case "INTEGER":
			case "DOUBLE":
			case "FLOAT":
			case "SHORT":
			case "BYTE":
			case "LONG":
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(Double.parseDouble(String.valueOf(valueToSet)));
				break;
			case "DATE":
				CellStyle cellStyle = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MMM/yyyy HH:mm:ss.ms"));
				cell.setCellValue((Date) valueToSet);
				cell.setCellStyle(cellStyle);
				break;
			case "BOOLEAN":
				cell.setCellValue(Boolean.parseBoolean(String.valueOf(valueToSet)));
				cell.setCellType(CellType.BOOLEAN);
				break;
			case "STRING":
			case "OBJECT":
			default:
				cell.setCellValue(String.valueOf(valueToSet));

				if (valueToSet.toString().toLowerCase().startsWith("=")) {
					cell.setCellType(CellType.FORMULA);
				} else {
					cell.setCellType(CellType.STRING);
				}
				break;
			}
		}

		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		fos.close();
	}

	public void setCellValue(String sheetName, int rowNum, int columnNum, Object valueToSet) throws IOException {
		setCellValue(workbook.getSheet(sheetName), rowNum, columnNum, valueToSet);
	}

	public List<List<Object>> getExcelData(Sheet sheet) {
		List<List<Object>> data = new ArrayList<List<Object>>();
		List<Object> values;

		for (Row row : sheet) {
			values = new ArrayList<Object>();
			for (Cell cell : row) {
				values.add(getCellValue(cell));
			}
			data.add(values);
		}
		return data;
	}

	public List<List<Object>> getExcelData(String sheetName) {
		return getExcelData(workbook.getSheet(sheetName));
	}

	public List<List<Object>> getExcelData(String sheetName, String filterConditions, boolean... strictCompareFlag) {
		List<List<Object>> data = new ArrayList<List<Object>>();
		boolean strictCompare = (strictCompareFlag != null && strictCompareFlag.length >= 1) ? strictCompareFlag[0]
				: false;

		Sheet sheet = workbook.getSheet(sheetName);
		int[] targetRowIndices = getRowIndices(sheet, filterConditions, strictCompare);

		if (targetRowIndices.length == 1 && targetRowIndices[0] == -1) {
			return data;
		} else {
			List<List<Object>> newData = getExcelData(sheet);

			for (int i : targetRowIndices) {
				data.add(newData.get(i));
			}
		}
		return data;
	}

	public List<List<Object>> getExcelData(String sheetName, String filterConditions, String columnNames,
			boolean... strictCompareFlag) {
		List<List<Object>> data = new ArrayList<>();
		boolean strictCompare = (strictCompareFlag != null && strictCompareFlag.length >= 1) ? strictCompareFlag[0]
				: false;
		Sheet sheet = workbook.getSheet(sheetName);
		int[] targetRowIndices = getRowIndices(sheet, filterConditions, strictCompare);
		if (targetRowIndices.length == 1 && targetRowIndices[0] == -1) {
			return data;
		} else {
			List<List<Object>> newData = getExcelColumnsData(sheetName, columnNames);
			for (int i : targetRowIndices) {
				data.add(newData.get(i-1));
			}
		}
		return data;
	}

	public List<List<List<Object>>> getExcelData() {
		List<List<List<Object>>> data = new ArrayList<List<List<Object>>>();

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			data.add(getExcelData(workbook.getSheetAt(i)));
		}
		return data;
	}

	public List<List<Object>> getExcelColumnsData(String sheetName, String columnNames) {
		List<List<Object>> data = new ArrayList<>();
		Sheet sheet = workbook.getSheet(sheetName);

		String[] arrColumns = columnNames.split(Constants.CONDITION_SEPARATOR);
		List<Object> values;

		for (Row row : sheet) {
			values = new ArrayList<>();

			if (row.getRowNum() != 0) {
				for (String string : arrColumns) {
					int colIndex = getColumnIndex(sheet, string);
					Cell cell = row.getCell(colIndex);
					values.add(getCellValue(cell));
				}
				data.add(values);
			}
		}
		return data;
	}

	private int[] getRowIndices(Sheet sheet, String filterCondition, boolean strictCompare) {
		ArrayList<Integer> list = new ArrayList<>();
		String[] conditions = filterCondition.split(Constants.CONDITION_SEPARATOR);
		LinkedHashMap<String, String> fullConditions = new LinkedHashMap<String, String>();

		for (String condition : conditions) {
			if (!condition.isEmpty()) // Added this condition to avoid exception
										// when blank/no condition is given
			{
				fullConditions.put(condition.split(Constants.CONDITIONVALUE_SEPARATOR)[0],
						condition.split(Constants.CONDITIONVALUE_SEPARATOR)[1]);
			}
		}

		int[] columnIndices = new int[fullConditions.size()];
		Set<String> columnNames = fullConditions.keySet();

		for (String columnName : columnNames) {
			columnIndices = ArrayUtils.add(columnIndices, getColumnIndex(sheet, columnName));
			columnIndices = ArrayUtils.remove(columnIndices, 0);
		}

		for (Row row : sheet) {
			LinkedHashMap<String, String> newHashMap = new LinkedHashMap<>();

			for (int index : columnIndices) {
				newHashMap.put(String.valueOf(getCellValue(sheet.getRow(0).getCell(index))),
						String.valueOf(getCellValue(row.getCell(index))));
			}

			if (strictCompare) {
				if (newHashMap.equals(fullConditions))
					list.add(row.getRowNum());
			} else {
				if (compareHashMapLoosely(newHashMap, fullConditions))
					list.add(row.getRowNum());
			}
		}

		if (list.size() == 0)
			list.add(-1);

		return ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
	}

	public ArrayList<Object> getEntireColumnData(Sheet sheet, int columnIndex) {
		ArrayList<Object> data = new ArrayList<>();
		for (Row row : sheet) {
			data.add(getCellValue(row.getCell(columnIndex)));
		}
		return data;
	}

	public ArrayList<Object> getEntireColumnData(String sheetName, int columnIndex) {
		return getEntireColumnData(workbook.getSheet(sheetName), columnIndex);
	}

	public ArrayList<Object> getEntireColumnData(String sheetName, String columnName) {
		ArrayList<Object> data = new ArrayList<>();
		data.addAll(getEntireColumnData(sheetName, getColumnIndex(workbook.getSheet(sheetName), columnName)));
		data.remove(0);
		return data;
	}

	private Connection getConnection(String driver, String connString) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		return DriverManager.getConnection(connString);
	}

	public HashMap<Integer, List<Object>> queryExcel(String query, boolean... includeColumnName)
			throws ClassNotFoundException, SQLException {
		String connString = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + excelPath;
		boolean blnColInclude = (includeColumnName != null && includeColumnName.length >= 1) ? includeColumnName[0]
				: false;

		Connection conn = getConnection("sun.jdbc.odbc.JdbcOdbcDriver", connString);
		Statement statement = null;
		ResultSet result = null;

		HashMap<Integer, List<Object>> dataSet = new HashMap<>();
		List<Object> data;
		int rowCounter = 1;

		try {
			statement = conn.createStatement();
			result = statement.executeQuery(query);

			while (result.next()) {
				int columnsCount = result.getMetaData().getColumnCount();
				data = new ArrayList<Object>();

				for (int i = 1; i <= columnsCount; i++) {
					Object cellValue = null;

					if (StringUtils.isNoneBlank(String.valueOf(result.getObject(i)))) {
						cellValue = "";
					} else {
						cellValue = result.getObject(i);
					}

					if (blnColInclude) {
						data.add(result.getMetaData().getColumnName(i) + Constants.COLUMN_SEPARATOR + cellValue);
					} else {
						data.add(cellValue);
					}
				}
				dataSet.put(rowCounter++, data);
			}
		} finally {
			result.close();
			statement.close();
			conn.close();
		}
		return dataSet;
	}

	public static String createExcelQuery(String sheetName, String queryColumns, String filter) {
		return "Select " + queryColumns + " from [" + sheetName + "$] where " + filter;
	}

	public static String createExcelQuery(String sheetName, String queryColumns) {
		return "Select " + queryColumns + " from [" + sheetName + "$]";
	}
}