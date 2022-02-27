# SuperSpineViewer

[**详细说明请看原链接**](https://github.com/Aloento/SuperSpineViewer/blob/master/README.md)

### 使用前注意额外下载此依赖：[**通用运行依赖**](https://github.com/Aloento/SuperSpineViewer/releases/tag/R1.0.0)并覆盖[**此ffmpeg版本**](https://www.gyan.dev/ffmpeg/builds/packages/ffmpeg-5.0-full_build.7z)

## 本工具在v1.2.6的基础上进行了以下修改，以方便日常使用：
### v1.2
-添加了文件列表功能，可以一次性拖拽大量文件至左下的列表控件中，点击sekl或Json文件可以直接加载显示（注意不要点其他类型文件，会卡死只能重开）（再注意：请勿拖拽多文件至主界面左上方区域，那边的功能还是拖拽单个文件打开，代码上没有做区分，所以需要拖拽大量文件的情况请注意确保拖拽到左下方的列表框中，否则会卡死需要重开）

-配置了自动播动画功能，现在所有所有状态修改和新加载模型都会自动播放动画了

-修复了1.1版本不能导出MP4文件的BUG，这是因为我首次使用GitHub的原因，导致关键性文件的代码被覆盖为原作者的代码，所以自然就丢失了新增的转MP4功能，现已修正
### v1.1

-添加文件拖拽打开功能，可以将skel或Json文件直接拖拽至主界面左侧选项卡的任意位置处读取模型

-为缩放比例、坐标X、坐标Y的输入框添加了鼠标滚轮修改数值功能，现在可以用鼠标滚轮轻松缩放模型大小和微调XY轴了，如需大幅调整XY轴可以选择单次点击滑块或直接输入数值

-修复BUG

### v1.0

-完全汉化

-分别在缩放比例、坐标X、坐标Y的输入框下方各添加了一个滑块，现在可以直接拖拽滑块来修改输入框里的值了，比起键盘输入还是用鼠标更为方便

-重新配置了左侧选项栏的布局，使用更加便捷

-输出格式添加MP4，使用的是libx264 crf26参数，大立绘的输出大小在500K~2MB左右，非常适合作为预览文件保存及传播

## 目前已知BUG：

### 原生BUG
0.已多次测试确认，原版代码状态下就不可以直接debug模式下加载模型，会报一个依赖的包未知的错，需要打包成.jar后才可以正常加载模型，所以修改版也同样有这个问题，需要先构建为jar再加载

1.目前渲染实现会占用大量不必要的带宽，请确保至少4G空闲内存再尝试输出文件

2.部分版本模型加载后会有白边

3.尸块名称含有空格的加载不了，需要把atlas和动画所有名称中的空格都去掉

4.遇到无法加载的模型或其他任何错误都会永久卡住，再做任何操作都不会有反应，需要重启工具解决

### 修改版BUG

1.持续拖拽3个新增的滑块控件会导致画面刷新缓慢，是因为拖动过程中持续修改数值的原因，建议点按滑块或者直接使用鼠标滚轮微调数值

2.通过拖拽3个新增的滑块控件修改的数值不能让文本框同步更新数值，这是代码本身的写法问题懒得处理

3.文件列表加载模型的功能使用的是原作者代码，他是在选择文件时限制了文件后缀，但是由于直接拖拽所以无法限制拖入的文件类型，也就没有对文件的过滤，所以一旦点击非sekl或Json文件会导致报错卡死，重开即可解决

## 超出我能力的还需要处理的事：
1.内存性能问题，原本没处理的问题

2.spine2.1版本支持

3.透明度问题，部分模型有白边，经过对比发现SpineViewerWPF在菜单栏Attributes里第一二行选项他比官方的skeletonViewer多一个SpineUseAlpha选项，经过反复对比发现skeletonViewer应该是默认开启最基础的这个Alpha功能的，而SuperSpineViewer刚好缺失了这个功能，只有PreAlpha，所以会导致一些模型会有白边问题。
在SpineViewerWPF中，无论先后顺序，只要同时勾选了这两种Alpha模式就会让白边消失，而在skeletonViewer中直接不开启PreAlpha就是无白边的。
至于怎么搞我反正不懂。
