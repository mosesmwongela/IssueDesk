package com.ictlife.issuedesk.util

import android.content.Context
import android.util.Log
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

val TAG: String? = "Utility"

class Utility {

    companion object {

//        fun getSpannableString(ctx: Context, string: String): SpannableString {
//            val str = SpannableString(string)
//            str.setSpan(TypefaceSpan(ctx, "quicksand_bold.ttf"), 0, str.length,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            return str
//        }

        fun formatPhoneNum(phone: String, context: Context, countryCode: String): String {
            var phoneUtil = PhoneNumberUtil.createInstance(context);
            var numberProto = phoneUtil.parse(phone, countryCode);
            return phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        }

        fun isKenyanPhoneNUmber(phone: String): Boolean {
            try {
                var kenyanPhoneNumberPattern = Pattern.compile("^(?:254|\\+254|0)?(7[0-9]{8})$")
                val matcher = kenyanPhoneNumberPattern.matcher(phone)
                return matcher.matches()
            } catch (e: Exception) {
                return false
            }
        }

        fun validatePhoneNumber(number: String, countryCode: String, context: Context): Boolean {

            lateinit var phoneNumberUtil: PhoneNumberUtil
            phoneNumberUtil = PhoneNumberUtil.createInstance(context)

            var validatedNumber = if (number.startsWith("+")) number else "+$number"

            try {
                if (countryCode.equals("KE")) {
                    return isKenyanPhoneNUmber(number)
                }
                phoneNumberUtil.parse(validatedNumber, countryCode)
                return true
            } catch (e: NumberParseException) {
                Log.e(TAG, "error during parsing a number: " + validatedNumber + " of country code: " + countryCode)
                return false
            }
        }

        fun getCountryIsoCode(number: String, context: Context): String? {

            lateinit var phoneNumberUtil: PhoneNumberUtil
            phoneNumberUtil = PhoneNumberUtil.createInstance(context)

            val validatedNumber = if (number.startsWith("+")) number else "+$number"

            val phoneNumber = try {
                phoneNumberUtil.parse(validatedNumber, null)
            } catch (e: NumberParseException) {
                Log.e(TAG, "error during parsing a number")
                null
            }
            if (phoneNumber == null) return null

            return phoneNumberUtil.getRegionCodeForCountryCode(phoneNumber.countryCode)
        }

        fun getPhoneNUmber(number: String, countryCode: String, context: Context): String? {

            val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)

            val countryCodeNum = "+" + phoneNumberUtil.getCountryCodeForRegion(countryCode)

            return number.replace(countryCodeNum, "")
        }

        //for display on the general trans item
        fun generalFormatDateTime(time: String, date: String): String {
            var date_time = ""
            try {
                date_time = (time
                        + " on " +
                        FormatDate(date, "yyyy-MM-dd", "EEE")
                        + " "
                        + FormatDate(date, "yyyy-MM-dd", "dd")
                        + dayOfMonthSuffix(Integer.parseInt(FormatDate(date, "yyyy-MM-dd", "dd"))))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return date_time
        }

        fun FormatDate(date: String, format_from: String, format_to: String): String {
            try {
                val dateFormat = SimpleDateFormat(format_from, Locale.US)
                val date1 = dateFormat.parse(date)

                val dateFormat2 = SimpleDateFormat(format_to, Locale.US)
                return dateFormat2.format(date1!!)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return date
        }

        fun dayOfMonthSuffix(day: Int): String {
            if (day >= 11 && day <= 13) {
                return "th"
            }
            when (day % 10) {
                1 -> return "st"
                2 -> return "nd"
                3 -> return "rd"
                else -> return "th"
            }
        }

        fun formatAmountToCurrency(amount: Double): String {
            try {
                val formatter = DecimalFormat("###,###,##0.00")
                return formatter.format(amount)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "0.00"
        }


    }

}