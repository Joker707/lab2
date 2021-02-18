# Цели
- Ознакомиться с жизненным циклом Activity
- Изучить основные возможности и свойства alternative resources

## Вариант 16

## Задача 1 - Activity

Жизненный цикл решил сразу смотреть на примере __continuewatch__.

- В самом начале, при запуске приложения, у нас вызываются 3 метода: onCreate, onStart, onResume.
- При повороте экрана: onPause - onSaveInstanceState - onStop - onDestroy - onCreate - onStart - onRestoreInstanceState - onResume
- При нажатии на Overview: onPause - onSaveInstanceState - onStop - onRestart - onStart - onResume
- При нажатии на Home: аналогично Overview
- При выключении экрана и последующем включении: аналогично Overview
- При выключении устройства: onPause - onSaveInstanceState - onStop

__Листинг 1.1__

    package com.example.lab2
    
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import kotlinx.android.synthetic.main.activity_main.*
    
    class ContinueWatch : AppCompatActivity() {
        var secondsElapsed: Int = 0
        var visibility = false
    
        var backgroundThread = Thread {
            while (true) {
                Thread.sleep(1000)
                if (visibility) {
                    textSecondsElapsed.post {
                        textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed++
                    }
                }
            }
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            backgroundThread.start()
            Log.d("Lifecycle", "onCreate")
        }
    
        override fun onStart() {
            visibility = true
            super.onStart()
            Log.d("Lifecycle", "onStart")
        }
    
        override fun onPause() {
            super.onPause()
            Log.d("Lifecycle", "onPause")
        }
    
        override fun onRestart() {
            super.onRestart()
            Log.d("Lifecycle", "onRestart")
        }
    
        override fun onResume() {
            super.onResume()
            Log.d("Lifecycle", "onResume")
        }
    
        override fun onStop() {
            visibility = false
            super.onStop()
            Log.d("Lifecycle", "onStop")
        }
    
        override fun onDestroy() {
            super.onDestroy()
            Log.d("Lifecycle", "onDestroy")
        }
    
        override fun onSaveInstanceState(outState: Bundle) {
            outState.putInt("seconds", secondsElapsed)
            super.onSaveInstanceState(outState)
            Log.d("Lifecycle", "onSaveInstanceSaved")
        }
    
        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
            secondsElapsed = savedInstanceState.getInt("seconds")
            super.onRestoreInstanceState(savedInstanceState)
            Log.d("Lifecycle", "onRestoreInstanceState")
        }
    }

__Листинг 1.2__

    <?xml version="1.0" encoding="utf-8"?>
    <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ContinueWatch">
    
        <TextView
            android:id="@+id/textSecondsElapsed"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    
    </androidx.constraintlayout.widget.ConstraintLayout>

__Листинг 1.3__

    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.example.lab2">
    
        <application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:theme="@style/AppTheme">
            <activity android:name=".ContinueWatch" >
                <intent-filter>
                    <action android:name="android.intent.action.MAIN" />
                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
            </activity>
            <activity android:name=".MainActivity"/>
        </application>
    
    </manifest>

## Задача 2 - Alternative Resources

Продемонстрируйте работу альтернативного ресурса - __MCC/MNC__.

Я очень долго сидел и тупил, что от меня хотят в этом задание, и как реализовать данную штуку.
В итоге, я просто влепил вывод на экран значения MCC/MNC и строчку, в которой показывается
Российский ли это оператор связи.
Что-то мне в глубине души подсказывает, что от меня хотели не совсем этот гениальный мув....


__Листинг 2.1__

    <?xml version="1.0" encoding="utf-8"?>
    <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
    
        <TextView
            android:id="@+id/mcc_mnc"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    
        <TextView
            android:id="@+id/where_you_are"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/mcc_mnc"  />
    
    </androidx.constraintlayout.widget.ConstraintLayout>

Переписал немного __MainActivity__:
__Листинг 2.2__

    package com.example.lab2
    
    import android.content.Context
    import android.os.Bundle
    import android.telephony.TelephonyManager
    import androidx.appcompat.app.AppCompatActivity
    import kotlinx.android.synthetic.main.activity_main2.*
    
    
    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main2)
            mcc_mcn()
        }
    
        fun mcc_mcn() {
            var mcc = 0
            var mnc = 0
            val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val networkOperator = tel.networkOperator
    
            if (networkOperator == "") {
                mcc_mnc.text = "INSERT YOUR SIM"
            } else {
                mcc = networkOperator.substring(0, 3).toInt()
                mnc = networkOperator.substring(3).toInt()
                mcc_mnc.text = "MCC: " + mcc + " MNC: " + mnc
                if (mcc != 250) {
                    where_you_are.text = "Russia is missing you, mate!"
                } else {
                    where_you_are.text = "You are at home, mate!"
                }
            }
        }
    }

## Задача 3 - Best-matching resource

Конфигурация устройства:
LOCALE_LANG: en
LOCALE_REGION: rFR
SCREEN_SIZE: large
SCREEN_ASPECT: long
ROUND_SCREEN: notround
ORIENTATION: port
UI_MODE: watch
NIGHT_MODE: notnight
PIXEL_DENSITY: ldpi
TOUCH: finger
PRIMARY_INPUT: nokeys
NAV_KEYS: dpad
PLATFORM_VER: v27

Конфигурация ресурсов:
(default)
rCA-notnight-nokeys
fr-large-port
en-car-night-tvdpi-12key-v26
en-small-long-notround-port-xxxhdpi
hdpi
rUS-television-notnight
en-notround-desk-notouch
small-notround-land-finger-v25
round-port-notnight-xxhdpi-v27
fr-rCA-normal-long-port-finger

- Убираем 2 строки с языками fr

(default)
rCA-notnight-nokeys
__~~fr-large-port~~__
en-car-night-tvdpi-12key-v26
en-small-long-notround-port-xxxhdpi
hdpi
rUS-television-notnight
en-notround-desk-notouch
small-notround-land-finger-v25
round-port-notnight-xxhdpi-v27
__~~fr-rCA-normal-long-port-finger~~__

- Убираем 2 строки с регионами rCA и rUS

(default)
__~~rCA-notnight-nokeys~~__
en-car-night-tvdpi-12key-v26
en-small-long-notround-port-xxxhdpi
hdpi
__~~rUS-television-notnight~~__
en-notround-desk-notouch
small-notround-land-finger-v25
round-port-notnight-xxhdpi-v27

- Смотрим на размер экрана и убираем 2 лишние строчки

(default)
en-car-night-tvdpi-12key-v26
__~~en-small-long-notround-port-xxxhdpi~~__
hdpi
en-notround-desk-notouch
__~~small-notround-land-finger-v25~~__
round-port-notnight-xxhdpi-v27

- Смотрим на ROUND_SCREEN

(default)
en-car-night-tvdpi-12key-v26
hdpi
en-notround-desk-notouch
__~~round-port-notnight-xxhdpi-v27~~__

- Смотрим на UI_MODE

(default)
__~~en-car-night-tvdpi-12key-v26~~__
hdpi
__~~en-notround-desk-notouch~~__

- Смотрим на PIXEL_DENSITY и оставляем единственно возможный вариант - __default__

(default)
__~~hdpi~~__


## Задача 4 - Сохранение состояние Activity

Ошибки, которые исправил:

- Убрал атрибут text у TextView, чтобы не отображалось "Hello World"
- Добавил константу для сохранения секунд при перезапуске
- Добавил остановку и продолжение счета при onPause и onResume
- Попробовал восстанавливать состояние как в onCreate, так и в onRestoreInstanceState

Как и сказано в самом задании, мы должны были использовать методы onSaveInstanceState 
и onRestoreInstanceState, которые записывают текущее состояние в глобальную переменную 
и берут из неё число при возобновлении.

## Вывод

Затраченное время на выполнение работы и составление отчета - 6 часов.

Так как я совместил выполнение 1 и 4 пунктов, то время немного сэкономилось.
Сидел и долго думал, как мне вообще что-то написать для MCC/MNC. В итоге решил просто выводить
эти циферки на экран и выводить информацию, Российские это номера или нет....
Сомнительная, конечно, штука для альтернативных ресурсов, поэтому, видимо, это неправильно :)
На Task3 потратил минут 30, но зато сразу вносил всё в отчет.