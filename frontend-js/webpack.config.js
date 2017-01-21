var fp = require("path")
var webpack = require("webpack");
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var config = require("./config")(process.argv);

module.exports = {
    entry: {
        app: "./src/app.js",
        vendor: [
            "vue",
            "vue-router",
            "vuex"
        ]
    },
    output: {
        path: __dirname + '/public/assets',
        publicPath: config.server.assetPath,
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: {
                    loaders: {
                        css: ExtractTextPlugin.extract({
                            loader: 'css-loader',
                            fallbackLoader: 'vue-style-loader' // <- this is a dep of vue-loader, so no need to explicitly install if using npm3
                        }),
                        'scss': 'vue-style-loader!css-loader!sass-loader',
                        //'sass': 'vue-style-loader!css-loader!sass-loader?indentedSyntax'
                    }
                }
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.(png|jpe?g|gif|svg|eot|otf|woff2|woff|ttf)(\?.*)?$/,
                loader: 'file-loader'
            },
            {
                test: /\.(json)$/,
                loader: 'json-loader'
            }
        ]
    },
    resolve: {
        extensions: ['.js', '.html', '.scss'],
        alias: {
            'vue$': 'vue/dist/vue.common.js'
        }
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify('production')
            }
        }),
        new webpack.optimize.CommonsChunkPlugin({
            names: 'vendor'
        }),
        new ExtractTextPlugin('styles.css')
        /*,
         new webpack.optimize.UglifyJsPlugin({
         compress: {
         warnings: false
         }
         })
         */
    ],
    devtool: 'cheap-source-map',
    devServer: {
        historyApiFallback: true,
        noInfo: true
    },
    performance: {
        hints: false
    }
}
;
