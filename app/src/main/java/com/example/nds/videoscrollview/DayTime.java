package com.example.nds.videoscrollview;

public class DayTime {
    public static final int THREE_HOURS = 1;
    public static final int ONE_HOUR = 2;
    public static final int HALF_HOUR = 3;
    public static final int FIFTEEN_MINUTES = 4;
    public static final int FIVE_MINUTES = 5;
    public final static int SECS_IN_TIME = 86400;

    private int zoomLevel = 1;

    public ThreeHours[] allHours = new ThreeHours[8];

    public DayTime(){
        for (int i = 0; i <allHours.length; i++){
            int startSec = ThreeHours.SECS_IN_TIME*i;
            int endSec = ThreeHours.SECS_IN_TIME*(i+1)-1;
            allHours[i] = new ThreeHours(startSec,endSec);
        }

    }

    public class ThreeHours {
        public final static int SECS_IN_TIME = 10800;
        private int mStartSec = 0;
        private int mEndSec = 0;
        public  OneHour[] threeHours = new OneHour[3];


        public ThreeHours(int startSec, int endSec) {
            mStartSec = startSec;
            mEndSec = endSec;
        }
    }

    public class OneHour {
        public final static int SECS_IN_TIME = 3600;
        private int mStartSec = 0;
        private int mEndSec = 0;

        public OneHour(int startSec, int endSec) {
            mStartSec = startSec;
            mEndSec = endSec;
        }
    }

    public class HalfHour {
        public final static int SECS_IN_TIME = 1800;
        private int mStartSec = 0;
        private int mEndSec = 0;

        public HalfHour(int startSec, int endSec) {
            mStartSec = startSec;
            mEndSec = endSec;
        }
    }

    public class FifteenMinutes {
        public final static int SECS_IN_TIME = 900;
        private int mStartSec = 0;
        private int mEndSec = 0;

        public FifteenMinutes(int startSec, int endSec) {
            mStartSec = startSec;
            mEndSec = endSec;
        }
    }

    public class FiveMinutes {
        public final static int SECS_IN_TIME = 600;
        private int mStartSec = 0;
        private int mEndSec = 0;

        public FiveMinutes(int startSec, int endSec) {
            mStartSec = startSec;
            mEndSec = endSec;
        }
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
}
