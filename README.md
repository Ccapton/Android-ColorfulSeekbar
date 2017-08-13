# Android-ColorfulSeekbar

ColorfulSeekbar这个控件是我根据前作ColorfulProgressbar https://github.com/Ccapton/Android-ColorfulProgressBar 
变化而来的，进度条与之相比没有变化，功能与原生Seekbar没差别。

![](https://raw.githubusercontent.com/Ccapton/Android-ColorfulSeekbar/master/ColofulSeekbarDemo.gif)

具体用法与ColorfulProgressbar类似，这里不再赘述。

demo下载： https://raw.githubusercontent.com/Ccapton/Android-ColorfulSeekbar/master/ColorfulSeekbarDemo.apk
 
### 如何配置
build.gradle(Project)
``` code
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
build.gradle(Module:app)
``` code
 dependencies {
	      compile 'com.github.Ccapton:Android-ColorfulSeekbar:1.0'
	}
```
 
