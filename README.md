# AndroidBaseLibs - 基础组件
封装mvp/mvvm架构, 工具类，广告/链(接口 + 谷歌，facebook，小米..实现)，数据存取(接口 + mmkv默认实现)，网络(coroutine + retrofit + 自定义缓存 + 异常处理)，
权限(接口 + permissionX默认实现)，图片加载(接口 + Coil默认实现)，自定义控件(下拉刷新/加载更多/分页/与加载)，弹窗(顶部/底部弹窗)，全捕获异常，
获取媒体资源(音频/视频/图片)，业务链，定时任务，轮训任务

- core：底层基类/工具类/基础功能模块
  - app包：App全局提供context
  - base包：AbsActivity/AbsFragment/AbsDialog/AbsCenterDialg/AbsBottomDialog
  - ext包：常用扩展方法/顶层静态方法，Logger/Toast/...
  - helper包: kotlin类辅助方法/工具类, ActivityHelper/DisplayHelper/FileHelper/SPHelper
  - util包：java工具类，StringUtils/FileUtils/TimerUtils
  - function
    - datastore：封装数据存储模块抽象基类和默认实现类，默认使用SP实现数据存储
    - imageloader包：封装图片加载模块抽象基类和默认实现类
    - permission包：封装权限模块抽象基类和默认实现类，包括统一请求流程/提示弹窗/跳转弹窗
    - ad包：广告库核心组建

- mvp包：MVP架构的基类, BaseMvpActivity/BaseMvpFragment
- mvvm包：MVVM架构的基类，BaseMvvmActivity/BaseMvvmFragment/BaseViewModel/
- network: 封装Retrofit网络请求框架，协程/缓存/错误处理/统一配置
- datastore-mmkv：mmkv实现的数据存储
- imageloader-glide： glide实现的图片加载
- permission-andpermisson：AndPermission框架实现的请求权限