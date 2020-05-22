# Amalgamate
### Idea
To keep myself up to date with the mobile world, I have a list of sites / subreddits which I visit everyday. This really is a hassle and so the idea for **Amalgamate** was born. Amalgamate combines information from many sources - namely:
- r/androiddev
- r/kotlin
- kotlinlang.org
- r/iOSProgramming
- r/swift
Keeping an eye on all those sites is just one click away now. 

As core navigation a BottomBar with a NavigationItem for each topic (Android, iOS) is used. As a default - since I am way more interested in Android - Android is shown. The current active NavigationItem displays a RecyclerView with data combined from different sources surrounding the selected topic. When clicking on a RecyclerView item the original source opens using ChromeCustomTabs. Data from different timeframes can be shown. These include: day, week, month, year, all time. Amalgamate also supports a dark and a light mode, with the dark mode being the default.

### Implementation Details
The following dependencies are used:
- Ktx
- Material Components
- CustomTabs
- RxJava, RxKotlin, RxAndroid
- OkHttp
- Moshi

When the initial App start happens, a Splashscreen animating the Amalgamate logo is shown. The animation is made with an AnimatedVectorDrawable using trimPath. The core App Architecture is MVVM with RxJava used in the ViewModel and LiveData to observe the ViewModel. MaterialComponents provided by Google are used for the App UI.

### Showcase Video
![](https://imgur.com/hwcxQ2P.gif)
