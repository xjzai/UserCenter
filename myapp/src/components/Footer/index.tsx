import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
const Footer: React.FC = () => {
  const defaultMessage = 'xjzai1技术部出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'game',
          title: '游戏入口',
          href: 'http://www.xjzai1.fun',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/xjzai',
          blankTarget: true,
        },
        {
          key: 'divination',
          title: '卜卦入口',
          href: 'http://www.xjzai1.fun/divination',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
