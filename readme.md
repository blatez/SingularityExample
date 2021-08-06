# Singularity-Atom  
扩展,仅仅是扩展,不应该影响或者屏蔽原本的功能,仅仅是为了快速开发.平衡开发效率和学习成本!

---

## singularity  
基础封装.基础,适用所有的封装  
### 存储管理
* 存储目录  
    * FileProvider  
    * 容量管理
* 相机管理
    * 系统相机管理  

### UI
* RecyclerView-ITemDecoration  
    * LinearLayoutManager-Space  
    * GridLayouManager-Space  
    * LinearLayoutManager-Div  
    * GridLayoutManager-Div  
* 屏幕尺寸工具集  
* 列表数据状态(抽象)

### 数据管理  
* 媒体数据库  
    * 图片数据  

### 工具
* 调试模式开关  
* 日志(自动开关和手动开关)


## atom  
二次封装.简化操作抽取的共同组成部分  

---

### PagingPage(Fragment)  
* 对列表展示的封装  
* 数据提供抽象  
* 刷新  
* 加载  
* M-V抽象(Adapter)  
* 数据状态  
* 条目监听器提供  
### ImageSelector(Fragment)  
* 选择数量  
* 是否包含拍照  
### UI  
* 列表数据状态(实现)
