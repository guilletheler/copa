package com.gt.copa;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

/**
 * Created by rmpestano on 07/02/17.
 */
@Component
public class Utils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final SimpleDateFormat SDF_HMS = new SimpleDateFormat("HH:mm:ss");

	public static final SimpleDateFormat SDF_SLASH_DM = new SimpleDateFormat("dd/MM");

	public static final SimpleDateFormat SDF_SLASH_DMY = new SimpleDateFormat("dd/MM/yy");

	public static final SimpleDateFormat SDF_SLASH_DMYY = new SimpleDateFormat("dd/MM/yyyy");

	public static final SimpleDateFormat SDF_SLASH_DMYHM = new SimpleDateFormat("dd/MM/yy HH:mm");;

	public static final SimpleDateFormat SDF_SLASH_DMYHMS = new SimpleDateFormat("dd/MM/yy HH:mm:ss");;

	public static final SimpleDateFormat SDF_SLASH_DMYYHM = new SimpleDateFormat("dd/MM/yyyy HH:mm");;

	public static final SimpleDateFormat SDF_SLASH_DMYYHMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static final SimpleDateFormat SDF_ISO_YYMD = new SimpleDateFormat("yyyyMMdd");

	public static final SimpleDateFormat SDF_DMYY = new SimpleDateFormat("ddMMyyyy");

	public static final SimpleDateFormat SDF_YM = new SimpleDateFormat("yyyyMM");

	public static final SimpleDateFormat SDF_DMY = new SimpleDateFormat("ddMMyy");

	public static final SimpleDateFormat SDF_MD = new SimpleDateFormat("MMdd");

	public static final SimpleDateFormat SDF_SLASH_ISO_YYMDHM = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	public static final SimpleDateFormat SDF_BAR_ISO_YYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat[] DATE_FORMATS = new SimpleDateFormat[] { SDF_SLASH_DMY, SDF_SLASH_DMYY,
			SDF_SLASH_DMYHM, SDF_SLASH_DMYYHM, SDF_SLASH_DMYHMS, SDF_SLASH_DMYYHMS };

	public static final DecimalFormat DF_2E = new DecimalFormat("00");

	public static final DecimalFormat DF_4E = new DecimalFormat("0000");

	public static final DecimalFormat DF_2D = new DecimalFormat("0.00");

	public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

	public static <T> void addIfNotContains(List<T> list, T item) {
		if (!list.contains(item)) {
			list.add(0, item);
		}
	}

	public static Calendar getFirstDayOfMonth(Integer month) {
		Calendar desde = Calendar.getInstance();

		desde.add(Calendar.MONTH, month);

		desde.set(Calendar.DAY_OF_MONTH, 1);

		desde.set(Calendar.MILLISECOND, 0);
		desde.set(Calendar.SECOND, 0);
		desde.set(Calendar.MINUTE, 0);
		desde.set(Calendar.HOUR, 0);

		return desde;
	}

	public static Calendar getLastDayOfMonth(int month) {
		return getLastDayOfMonth(getFirstDayOfMonth(month));
	}

	public static Date getLastDayOfMonth(Date desde) {
		Calendar cdesde = Calendar.getInstance();
		cdesde.setTime(desde);
		return getLastDayOfMonth(cdesde).getTime();
	}

	public static Calendar getLastDayOfMonth(Calendar desde) {
		Calendar hasta = Calendar.getInstance();
		hasta.setTimeInMillis(desde.getTimeInMillis());
		hasta.add(Calendar.MONTH, 1);
		hasta.add(Calendar.DAY_OF_MONTH, -1);

		return hasta;
	}

	public static Integer dateToIntYYMD(Date fecha) {
		if (fecha == null) {
			return null;
		}
		return Integer.valueOf(SDF_ISO_YYMD.format(fecha));
	}
}
