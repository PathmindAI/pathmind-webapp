module.exports = {
  stories: ['./stories/**/*.stories.js'],  
  module: {
    rules: [
      {
        test: /\.css$/,
        use: ['to-string-loader', 'css-loader'],
      },
    ],
  },
  addons: ['@storybook/addon-backgrounds/register']
};
