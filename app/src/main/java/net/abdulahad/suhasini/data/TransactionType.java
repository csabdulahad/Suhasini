package net.abdulahad.suhasini.data;


import android.graphics.Color;

import net.abdulahad.suhasini.R;

public class TransactionType {

    public static final int OFFSET_FOR_TYPE = 2;

    public static final int DEPOSIT = 0;
    public static final int ADJUST_MONEY = 1;

    public static final int FOOD = 2;
    public static final int CLOTH = 3;
    public static final int HOUSE_RENT = 4;
    public static final int HELP = 5;
    public static final int TRAVEL = 6;
    public static final int MOBILE_INTERNET = 7;
    public static final int FUEL = 8;
    public static final int FIX = 9;
    public static final int HEALTH = 10;
    public static final int SUBSCRIPTION = 11;
    public static final int ELECTRIC_BILL = 12;
    public static final int STUDY = 13;
    public static final int MONEY_UNKNOWN = 14;

    public static int numOfType() {
        return 13;
    }

    public static int getIcon(int type) {
        int icon;
        switch (type) {
            case DEPOSIT:
                icon = R.drawable.ic_add;
                break;
            case ADJUST_MONEY:
                icon = R.drawable.ic_adjust;
                break;
            case FOOD:
                icon = R.drawable.ic_restaurant;
                break;
            case CLOTH:
                icon = R.drawable.ic_dry_cleaning;
                break;
            case HELP:
                icon = R.drawable.ic_volunteer;
                break;
            case TRAVEL:
                icon = R.drawable.ic_bus;
                break;
            case MOBILE_INTERNET:
                icon = R.drawable.ic_phone;
                break;
            case FUEL:
                icon = R.drawable.ic_fuel;
                break;
            case FIX:
                icon = R.drawable.ic_hardware;
                break;
            case HEALTH:
                icon = R.drawable.ic_hospital;
                break;
            case SUBSCRIPTION:
                icon = R.drawable.ic_hourglass_full;
                break;
            case HOUSE_RENT:
                icon = R.drawable.ic_house;
                break;
            case STUDY:
                icon = R.drawable.ic_school;
                break;
            case ELECTRIC_BILL:
                icon = R.drawable.ic_power;
                break;
            default:
                icon = R.drawable.ic_money_off;
        }
        return icon;
    }

    public static String getLabel(int type) {
        String title;
        switch (type) {
            case DEPOSIT:
                title = "Deposit";
                break;
            case ADJUST_MONEY:
                title = "Deposit Adjustment";
                break;
            case FOOD:
                title = "Food";
                break;
            case CLOTH:
                title = "Clothing";
                break;
            case HELP:
                title = "Help & Support";
                break;
            case TRAVEL:
                title = "Traveling";
                break;
            case MOBILE_INTERNET:
                title = "Mobile & Internet";
                break;
            case FUEL:
                title = "Vehicle Fuel";
                break;
            case FIX:
                title = "Fix & Upgrade";
                break;
            case HEALTH:
                title = "Health & Medical";
                break;
            case SUBSCRIPTION:
                title = "Subscription Fee";
                break;
            case HOUSE_RENT:
                title = "House Rent";
                break;
            case STUDY:
                title = "Education";
                break;
            case ELECTRIC_BILL:
                title = "Electric Bill";
                break;
            default:
                title = "Unknown";
        }
        return title;
    }

    public static int getColor(int type) {
        String title;
        switch (type) {
            case DEPOSIT:
                title = "#2E7D32";
                break;
            case ADJUST_MONEY:
                title = "#FBBC04";
                break;
            case FOOD:
                title = "#1976D2";
                break;
            case CLOTH:
                title = "#293A4A";
                break;
            case HELP:
                title = "#4A148C";
                break;
            case TRAVEL:
                title = "#D81B60";
                break;
            case MOBILE_INTERNET:
                title = "#BF360C";
                break;
            case FUEL:
                title = "#2578AF";
                break;
            case FIX:
                title = "#546E7A";
                break;
            case HEALTH:
                title = "#1A237E";
                break;
            case SUBSCRIPTION:
                title = "#2979FF";
                break;
            case HOUSE_RENT:
                title = "#01C855";
                break;
            case STUDY:
                title = "#00796B";
                break;
            case ELECTRIC_BILL:
                title = "#311B92";
                break;
            default:
                title = "#B71C1C";
        }
        return Color.parseColor(title);
    }

}
