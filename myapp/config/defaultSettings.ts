import { Settings as LayoutSettings } from '@ant-design/pro-components';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // 拂晓蓝
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: 'xjzai1的用户管理系统',
  pwa: false,
  logo: 'https://cdn.acwing.com/media/user/profile/photo/347318_lg_ec24767439.jpg',
  iconfontUrl: '',
};

export default Settings;
