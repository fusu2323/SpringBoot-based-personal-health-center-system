import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
    baseURL: 'http://localhost:9090',  // 注意这里直接写死后端地址，或者通过 import.meta.env.VITE_API_BASE
    timeout: 60000 // 延长到 60 秒
})

// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    
    // 从 localStorage 获取 token
    const user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    if (user && user.token) {
        config.headers['token'] = user.token;  // 设置请求头
    }
    
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 兼容字符串结果
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
             ElMessage.error('登录失效，请重新登录');
             localStorage.removeItem("user");
             // router.push("/login") // 这里不好直接引用 router，可以在组件里处理
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        ElMessage.error(error.message || '系统错误')
        return Promise.reject(error)
    }
)

export default request
